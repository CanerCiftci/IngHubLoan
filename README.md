# Inghub Loan Project

Bu proje, kullanıcıların kredi başvurusu yapabilmesi, ödemelerini gerçekleştirebilmesi ve taksitlerin durumunu takip edebilmesi için geliştirilmiştir.

## Özellikler

Kredi Oluşturma: Kullanıcılar kredi başvurusu yapabilirler.
Kredi Ödeme: Kullanıcılar, taksit ödemelerini gerçekleştirebilirler.
Taksit Listeleme: Kullanıcılar, kredi taksitlerini görüntüleyebilirler.
 
## Teknoloji 

Java 17
Spring Boot
H2 Database
Maven
JUnit 5
Lombok

## DB Connection Bilgileri [application.properties] içersinde yönetilmiştir, gerekli table create scriptleri resource.schema.sql içersindedir. 

## API EndPoint urlleri ->

Create Loan
http://localhost:8080/api/loans/createLoan

List Installments
http://localhost:8080/api/loans/listInstallments/1

Pay Loan
http://localhost:8080/api/loans/payLoan