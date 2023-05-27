package com.example.certificadoautofirmado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;


@SpringBootApplication
public class CertificadoAutoFirmadoApplication {

    public static void main(String[] args) {
//        SpringApplication.run(CertificadoAutoFirmadoApplication.class, args);
        try {
            // Generar una clave privada
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Crear el certificado X.509
            X509Certificate cert = AutoSignedCertificate.generateCertificate(keyPair);

            // Guardar el certificado en un archivo
            String filePath = "src/main/resources/static/certificado.crt";
            AutoSignedCertificate.saveCertificateToFile(cert, filePath);

            System.out.println("Certificado creado exitosamente en el archivo: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
