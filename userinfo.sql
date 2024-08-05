

-- Table: public.userinfo

-- DROP TABLE IF EXISTS public.userinfo;

CREATE TABLE IF NOT EXISTS public.userinfo
(
    userinfo_id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    isactive character(1) COLLATE pg_catalog."default" DEFAULT 'Y'::bpchar,
    created timestamp without time zone NOT NULL DEFAULT now(),
    createdby character varying(32) COLLATE pg_catalog."default",
    updated timestamp without time zone NOT NULL DEFAULT now(),
    updatedby character varying(32) COLLATE pg_catalog."default",
    name character varying(220) COLLATE pg_catalog."default" NOT NULL,
    value character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT userinfo_key PRIMARY KEY (userinfo_id),
    CONSTRAINT userinfo_value_key UNIQUE (value),
    CONSTRAINT userinfo_isactive_check CHECK (isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.userinfo
    OWNER to postgres;