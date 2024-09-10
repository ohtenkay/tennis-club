--liquibase formatted sql

--changeset ohtenkay:1
CREATE TABLE PhoneName (
    phoneNumber VARCHAR(50) UNIQUE PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    deletedAt TIMESTAMP
);

CREATE TABLE AppUser (
    id UUID PRIMARY KEY,
    phoneNumber VARCHAR(50) UNIQUE NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    deletedAt TIMESTAMP,
    FOREIGN KEY (phoneNumber) REFERENCES PhoneName(phoneNumber)
);

CREATE TABLE SurfaceType (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    pricePerMinute NUMERIC NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    deletedAt TIMESTAMP
);

CREATE TABLE Court (
    id UUID PRIMARY KEY,
    surfaceTypeId UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    deletedAt TIMESTAMP,
    FOREIGN KEY (surfaceTypeId) REFERENCES SurfaceType(id)
);

CREATE TABLE Reservation (
    id UUID PRIMARY KEY,
    courtId UUID NOT NULL,
    userId UUID NOT NULL,
    startTime TIMESTAMP NOT NULL,
    endTime TIMESTAMP NOT NULL,
    gameType VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    deletedAt TIMESTAMP,
    FOREIGN KEY (courtId) REFERENCES Court(id),
    FOREIGN KEY (userId) REFERENCES AppUser(id)
);
