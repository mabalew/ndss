-- Table: properties
CREATE TABLE properties (
  name character varying(35) NOT NULL,
  value character varying(10240),
  sgid character varying(35) NOT NULL DEFAULT 'UNKNOWN'::character varying,
  creation_date timestamp with time zone NOT NULL DEFAULT now(),
  last_access_date timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT pk_properties PRIMARY KEY (name , sgid )
)
  WITH (
    OIDS=FALSE
  );

-- Index: properties_idx
CREATE INDEX properties_idx
  ON properties
  USING btree
  (name COLLATE pg_catalog."default" , sgid COLLATE pg_catalog."default" );


