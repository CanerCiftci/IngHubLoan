CREATE TABLE CUSTOMER (
                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          NAME VARCHAR(255) NOT NULL,
                          SURNAME VARCHAR(255) NOT NULL,
                          CREDIT_LIMIT DECIMAL(15, 2) NOT NULL,
                          USED_CREDIT_LIMIT DECIMAL(15, 2) DEFAULT 0 NOT NULL
);

CREATE TABLE LOAN (
                      ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                      CUSTOMER_ID BIGINT NOT NULL,
                      LOAN_AMOUNT DECIMAL(15, 2) NOT NULL,
                      NUMBER_OF_INSTALLMENTS INT NOT NULL,
                      CREATE_DATE DATE NOT NULL,
                      IS_PAID INT DEFAULT 0 NOT NULL,
                      FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID)
);


CREATE TABLE LOAN_INSTALLMENT (
                                  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  LOAN_ID BIGINT NOT NULL,
                                  AMOUNT DECIMAL(15, 2) NOT NULL,
                                  PAID_AMOUNT DECIMAL(15, 2) DEFAULT 0 NOT NULL,
                                  DUE_DATE DATE NOT NULL,
                                  PAYMENT_DATE DATE,
                                  IS_PAID INT DEFAULT 0 NOT NULL,
                                  FOREIGN KEY (LOAN_ID) REFERENCES LOAN(ID)
);