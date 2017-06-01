package de.tud.cs.stg.zenodo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by benhermann on 01.06.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    public PreserveDOI preserve_doi;
    public String upload_type;
    public String publication_type;
    public String image_type;
    public Date publication_date;
    public String title;
    // public ArrayList<Creator> creators;
    public String description;
    public String access_right;
    public String license;
    public Date embargo_date;
    public String access_conditions;
    public String doi;
    // public boolean preserve_doi;
    public String keywords;
    public String notes;
    // public ArrayList<Identifier> related_identifiers;
    // public ArrayList<Contributor> contributers;
    // public ArrayList<String> references;
    // public ArrayList<Community> communities;
    // public ArrayList<Grant> grants;
    public String journal_title;
    public String journal_volume;
    public String journal_issue;
    public String journal_pages;
    public String conference_title;
    public String conference_acronym;
    public String conference_dates;
    public String conference_place;
    public String conference_url;
    public String conference_session;
    public String conference_session_part;
    public String imprint_publisher;
    public String imprint_isbn;
    public String imprint_place;
    public String partof_title;
    public String partof_pages;
    public String thesis_supervisors;
    public String thesis_university;
    // public ArrayList<Subject> subjects;


}
