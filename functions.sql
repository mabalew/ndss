-- Function: add_property(text, text, text)
CREATE OR REPLACE FUNCTION add_property(IN property_name text, IN property_value text, IN _sgid text, OUT result boolean)
  RETURNS boolean AS
  $BODY$DECLARE
   counter INTEGER;
  BEGIN
    SELECT INTO counter count(*) FROM properties WHERE name=property_name AND sgid=_sgid;
    IF counter > 0 THEN
      result := false;
    ELSE
      INSERT INTO properties(name, value, sgid) VALUES(property_name, property_value, _sgid);
      result := true;
    END IF;
  END;
  $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: delete_property(text, text)
CREATE OR REPLACE FUNCTION delete_property(IN property_name text, IN _sgid text, OUT result boolean)
  RETURNS boolean AS
  $BODY$DECLARE
   counter INTEGER;
  BEGIN
    SELECT INTO counter count(*) FROM properties WHERE name=property_name AND sgid=_sgid;
    IF counter = 1 THEN
      DELETE FROM properties WHERE name=property_name AND sgid=_sgid;
      result := true;
    ELSE
      result := false;
    END IF;
    RETURN;
  END;
  $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: get_property(text, text)
CREATE OR REPLACE FUNCTION get_property(IN property_name text, IN _sgid text, OUT property_value text)
  RETURNS text AS
  $BODY$DECLARE
   counter INTEGER;
  BEGIN
    SELECT INTO property_value value FROM properties WHERE name=property_name AND sgid=_sgid;
    UPDATE properties SET last_access_date = now() WHERE name=property_name AND sgid=_sgid;
  END;
  $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: list_properties(text)
CREATE OR REPLACE FUNCTION list_properties(_sgid text)
  RETURNS refcursor AS
  $BODY$
  DECLARE
   curs refcursor;
  BEGIN
    OPEN curs FOR SELECT name, value, last_access_date, creation_date as value FROM properties WHERE sgid=_sgid;
    UPDATE properties SET last_access_date = now() WHERE sgid=_sgid;
    return curs;
  END;
  $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: update_property(text, text, text)
CREATE OR REPLACE FUNCTION update_property(IN property_name text, IN property_value text, IN _sgid text, OUT result boolean)
  RETURNS boolean AS
  $BODY$DECLARE
   counter INTEGER;
  BEGIN
    result := false;
    SELECT INTO counter count(*) FROM properties WHERE name=property_name AND sgid=_sgid;
    IF counter > 0 THEN
      UPDATE properties SET value=property_value WHERE name=property_name AND sgid=_sgid;
      UPDATE properties SET last_access_date = now() WHERE name=property_name AND sgid=_sgid;
      result := true;
    END IF;
    END
    $BODY$
    LANGUAGE plpgsql VOLATILE
    COST 100;

