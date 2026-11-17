UPDATE company
SET company_name = regexp_replace(trim(company_name), '\s+', ' ', 'g')
WHERE company_name <> regexp_replace(trim(company_name), '\s+', ' ', 'g');
