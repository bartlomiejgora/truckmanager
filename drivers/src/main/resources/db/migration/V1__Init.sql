CREATE TABLE driver
(
    id         UUID NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    CONSTRAINT pk_driver PRIMARY KEY (id)
);

CREATE TABLE driver_licence
(
    id              UUID NOT NULL,
    driver_id       UUID,
    star_date       TIMESTAMP WITH TIME ZONE,
    expiration_date TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_driverlicence PRIMARY KEY (id)
);

ALTER TABLE driver_licence
    ADD CONSTRAINT FK_DRIVERLICENCE_ON_DRIVERID FOREIGN KEY (driver_id) REFERENCES driver (id);