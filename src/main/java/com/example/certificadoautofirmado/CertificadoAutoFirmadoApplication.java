package com.example.certificadoautofirmado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


@SpringBootApplication
public class CertificadoAutoFirmadoApplication {

    public static void main(String[] args) throws KeyStoreException {
//        SpringApplication.run(CertificadoAutoFirmadoApplication.class, args);
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try {
            // Cargar el almacén de certificados del sistema
            String keyStoreFile = System.getProperty("java.home") + "/lib/security/cacerts";
            char[] password = "changeit".toCharArray(); // Contraseña predeterminada del almacén de certificados
            keyStore.load(new FileInputStream(keyStoreFile), password);

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

            String alias = "semper_certificado"; // Alias para identificar el certificado
            keyStore.setCertificateEntry(alias, cert);

            // Guardar los cambios en el almacén de certificados
            keyStore.store(new FileOutputStream(keyStoreFile), password);

            System.out.println("Certificado guardado exitosamente en el almacén de certificados del sistema.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate certificate = keyStore.getCertificate(alias);

                // Mostrar información relevante del certificado, como el emisor y el sujeto
                if (certificate instanceof X509Certificate x509Certificate) {
                    System.out.println("Alias: " + alias);
                    System.out.println("Emisor (Issuer): " + x509Certificate.getIssuerDN());
                    System.out.println("Sujeto (Subject): " + x509Certificate.getSubjectDN());
                    System.out.println("----------------------------------------------");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
