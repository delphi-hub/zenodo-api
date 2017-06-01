package de.tud.cs.stg.zenodo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by benhermann on 31.05.17.
 */
public class ZenodoClient {

    private static final String productionToken = "GopFIrt1d7D219AR3ORCHIL33oe7vAmeht2pPr1ZxzOenLp5m4ynCQLLOUBD";
    private static final String sandboxToken = "WjNJFRWxsnDdnQoJdAZ92YZ6k69PXfBTqKmjuZphAtgG0Fs66orrre5sMWJe";

    private static final String sandboxURL = "https://sandbox.zenodo.org/";
    private static final String productionURL = "https://zenodo.org/";

    private abstract class MyObjectMapper implements ObjectMapper {
        public abstract <T> T readValue(String value, TypeReference<T> valueType);
    }

    private final MyObjectMapper objectMapper;

    private String baseURL;
    private String token;


    public ZenodoClient(String baseURL, String token) {
        this.baseURL = baseURL;
        this.token = token;

        objectMapper = new MyObjectMapper() {
            final ISO8601DateFormat dateFormat = new ISO8601DateFormat() {
                @Override
                public Date parse(String source) throws ParseException {
                    if (!source.endsWith("+0000")) source = source + "+0000";
                    return super.parse(source);
                }
            };
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            {
                jacksonObjectMapper.setDateFormat(dateFormat);
                jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public <T> T readValue(String value, TypeReference<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Unirest.setObjectMapper(objectMapper);
    }

    public boolean test() {
        GetRequest request = prepareGetRequest(baseURL + API.Deposit.Depositions);
        try {
            HttpResponse<String> response = request.asString();
            if (response.getStatus() == 200) return true;
        } catch (UnirestException e) {
        }

        return false;
    }

    public List<Deposition> getDepositions() {
        ArrayList<Deposition> result = new ArrayList<Deposition>();
        GetRequest request = prepareGetRequest(baseURL + API.Deposit.Depositions);
        try {
            ArrayList<Deposition> response = fromJSON(new TypeReference<ArrayList<Deposition>>() {}, request.asString().getBody());
            return response;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T fromJSON(final TypeReference<T> type, final String jsonPacket) {
        T data = null;
        try {
            data = objectMapper.readValue(jsonPacket, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    public Deposition createDeposition() {
        return createDeposition(null);
    }

    public Deposition createDeposition(final Metadata m) {
        HttpRequestWithBody post = preparePostRequest(baseURL + API.Deposit.Depositions);

        String data = "{}";
        if (m != null)
            data = objectMapper.writeValue(new Object() {
                public Metadata metadata = m;
            });
        RequestBodyEntity completePost = post.body(data);

        try {
            HttpResponse<Deposition> response = completePost.asObject(Deposition.class);
            return response.getBody();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean publish(Integer id) {
        HttpRequestWithBody post = preparePostRequest(baseURL + API.Deposit.Publish).routeParam("id",id.toString());

        try {
            final HttpResponse<String> response = post.asString();
            if (response.getStatus() == 202) return true;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean discard(Integer id) {
        HttpRequestWithBody post = preparePostRequest(baseURL + API.Deposit.Discard).routeParam("id",id.toString());

        try {
            final HttpResponse<String> response = post.asString();
            if (response.getStatus() == 201) return true;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

    private HttpRequestWithBody preparePostRequest(String url) {
        return Unirest.post(url)
                      .header("Content-Type", "application/json")
                      .header("Authorization", "Bearer " + token);
    }

    private GetRequest prepareGetRequest(String url) {
        return Unirest.get(url)
                .header("Authorization", "Bearer " + token);
    }

    public static void main(String[] args) {
        ZenodoClient client = new ZenodoClient(sandboxURL, sandboxToken);
        System.out.println(client.test());

        Metadata firstTry = new Metadata();
        firstTry.title = "API test";
        Deposition deposition = client.createDeposition(firstTry);
        System.out.println(deposition.id);

        /*
        // Produces status 500 currently.
        List<Deposition> depositions = client.getDepositions();
        for (Deposition d : depositions)
            client.discard(d.id);
        */
    }
}
