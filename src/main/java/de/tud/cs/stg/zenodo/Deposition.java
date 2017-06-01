package de.tud.cs.stg.zenodo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by benhermann on 31.05.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deposition {
    public Date created;
    public Integer id;
    public Date modified;
    public Integer owner;
    public Integer record_id;
    public String state;
    public String title;

    public Links links;
    public Metadata metadata;

}
