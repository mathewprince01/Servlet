-- Table: public.c_order

-- DROP TABLE IF EXISTS public.c_order;

CREATE TABLE IF NOT EXISTS public.c_order
(
    c_order_id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    created timestamp without time zone DEFAULT now(),
    createdby character varying(32) COLLATE pg_catalog."default",
    updated timestamp without time zone DEFAULT now(),
    updatedby character varying(32) COLLATE pg_catalog."default",
    order_date date NOT NULL,
    vendor character varying(200) COLLATE pg_catalog."default",
    edd date NOT NULL,
    shipping_address character varying(1200) COLLATE pg_catalog."default",
    issale boolean,
    isactive boolean,
    CONSTRAINT c_order_key PRIMARY KEY (c_order_id),
    CONSTRAINT c_order_createdby_fk FOREIGN KEY (createdby)
        REFERENCES public.userinfo (userinfo_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT c_order_updatedby_fk FOREIGN KEY (updatedby)
        REFERENCES public.userinfo (userinfo_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.c_order
    OWNER to postgres;