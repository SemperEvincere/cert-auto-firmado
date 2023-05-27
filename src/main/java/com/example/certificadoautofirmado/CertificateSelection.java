package com.example.certificadoautofirmado;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CertificateSelection {
    public static void main(String[] args) {
        try {
            // Obtener el almacén de certificados del sistema
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null); // Cargar el almacén de certificados del sistema

            // Obtener la lista de alias de certificados disponibles en el almacén
            Enumeration<String> aliases = keyStore.aliases();

            // Mostrar los certificados disponibles al usuario
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate certificate = keyStore.getCertificate(alias);

                // Mostrar información relevante del certificado, como el emisor y el sujeto
                if (certificate instanceof X509Certificate) {
                    X509Certificate x509Certificate = (X509Certificate) certificate;
                    System.out.println("Alias: " + alias);
                    System.out.println("Emisor (Issuer): " + x509Certificate.getIssuerDN());
                    System.out.println("Sujeto (Subject): " + x509Certificate.getSubjectDN());
                    System.out.println("----------------------------------------------");
                }
            }

            // Permitir que el usuario seleccione un certificado específico
            // Puedes implementar la lógica para que el usuario elija el certificado deseado

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
