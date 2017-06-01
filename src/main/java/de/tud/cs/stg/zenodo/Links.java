package de.tud.cs.stg.zenodo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by benhermann on 31.05.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links {
    public String bucket;
    public String discard;
    public String edit;
    public String files;
    public String html;
    public String latest_draft;
    public String latest_draft_html;
    public String publish;
    public String self;
}
