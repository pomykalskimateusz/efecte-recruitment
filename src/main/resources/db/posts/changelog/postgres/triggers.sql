create or replace function update_modified_date_column()
    returns trigger as '
    begin
        NEW.modified_date_timestamp = current_timestamp;
        return NEW;
    end;
' LANGUAGE PLPGSQL;

