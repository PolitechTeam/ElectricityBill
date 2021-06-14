CREATE TABLE IF NOT EXISTS `User`
(
    `Id`         INT         NOT NULL AUTO_INCREMENT,
    `Login`      VARCHAR(45) NOT NULL,
    `Password`   VARCHAR(45) NOT NULL,
    `Name`       VARCHAR(45) NOT NULL,
    `Surname`    VARCHAR(45) NOT NULL,
    `FatherName` VARCHAR(45) NOT NULL,
    `City`       VARCHAR(45) NOT NULL,
    `Street`     VARCHAR(45) NOT NULL,
    `House`      VARCHAR(45) NOT NULL,
    `Flat`       INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (`Id`)
);

CREATE TABLE IF NOT EXISTS `Bill`
(
    `Id`          INT  NOT NULL AUTO_INCREMENT,
    `UserId`      INT  NOT NULL,
    `Indication`  INT  NOT NULL,
    `PaymentDate` DATE NOT NULL,
    PRIMARY KEY (`Id`),
    CONSTRAINT `user_id` FOREIGN KEY (`UserId`) REFERENCES `User` (`Id`)
);

INSERT INTO User (Login, Password, Name, Surname, FatherName, City, Street, House, Flat)
VALUES ('testUser', '123456', 'Владимир', 'Астафьев', 'Дмитриевич', 'Москва', 'Пролетарская', '15', 5);