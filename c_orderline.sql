-- Table: public.c_orderline

-- DROP TABLE IF EXISTS public.c_orderline;

CREATE TABLE IF NOT EXISTS public.c_orderline
(
    c_orderline_id character varying(32) COLLATE pg_catalog."default" NOT NULL DEFAULT substr((uuid_generate_v4())::text, 1, 32),
    created timestamp without time zone DEFAULT now(),
    createdby character varying(32) COLLATE pg_catalog."default",
    updated timestamp without time zone DEFAULT now(),
    updatedby character varying(32) COLLATE pg_catalog."default",
    product character varying(200) COLLATE pg_catalog."default",
    umo character varying(20) COLLATE pg_catalog."default",
    quantity numeric,
    price numeric,
    c_order_id character varying(32) COLLATE pg_catalog."default",
    totalprice numeric,
    isactive boolean,
    issale boolean,
    CONSTRAINT c_orderline_id_key PRIMARY KEY (c_orderline_id),
    CONSTRAINT c_order_id_createdby_fk FOREIGN KEY (createdby)
        REFERENCES public.userinfo (userinfo_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT c_order_id_updatedby_fk FOREIGN KEY (updatedby)
        REFERENCES public.userinfo (userinfo_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT c_orderline_c_order_id_fk FOREIGN KEY (c_order_id)
        REFERENCES public.c_order (c_order_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.c_orderline
    OWNER to postgres;