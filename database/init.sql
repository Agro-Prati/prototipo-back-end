--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5

-- Started on 2025-08-15 15:21:15 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3370 (class 1262 OID 16384)
-- Name: agro_prototipo_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE agro_prototipo_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE agro_prototipo_db OWNER TO postgres;

\connect agro_prototipo_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 24576)
-- Name: tb_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_role (
    id uuid NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE public.tb_role OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24583)
-- Name: tb_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_user (
    id uuid NOT NULL,
    name character varying NOT NULL,
    email character varying NOT NULL,
    password character varying NOT NULL,
    role uuid NOT NULL
);


ALTER TABLE public.tb_user OWNER TO postgres;

--
-- TOC entry 3363 (class 0 OID 24576)
-- Dependencies: 217
-- Data for Name: tb_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tb_role (id, name) VALUES ('f4ac56f8-3f81-45fd-8738-ff4aaad7db52', 'ADMIN');
INSERT INTO public.tb_role (id, name) VALUES ('c797f1b1-d0b3-415b-a8f1-4fc1281af09f', 'USER');


--
-- TOC entry 3364 (class 0 OID 24583)
-- Dependencies: 218
-- Data for Name: tb_user; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3214 (class 2606 OID 24582)
-- Name: tb_role tb_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_role
    ADD CONSTRAINT tb_role_pkey PRIMARY KEY (id);


--
-- TOC entry 3216 (class 2606 OID 24589)
-- Name: tb_user tb_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_user
    ADD CONSTRAINT tb_user_pkey PRIMARY KEY (id);


--
-- TOC entry 3217 (class 2606 OID 24590)
-- Name: tb_user tb_user_role_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_user
    ADD CONSTRAINT tb_user_role_fkey FOREIGN KEY (role) REFERENCES public.tb_role(id);


-- Completed on 2025-08-15 15:21:15 UTC

--
-- PostgreSQL database dump complete
--

