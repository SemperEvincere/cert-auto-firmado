package com.example.certificadoautofirmado;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class AutoSignedCertificate {

    public static X509Certificate generateCertificate(KeyPair keyPair) throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        // Crear un objeto de solicitud de certificado (CSR)
        X509V3CertificateGenerator certGenerator = new X509V3CertificateGenerator();

        // Configurar el emisor del certificado
        X500Principal issuer = new X500Principal("CN=Issuer");
        certGenerator.setIssuerDN(issuer);

        // Configurar el sujeto del certificado
        X500Principal subject = new X500Principal("CN=Subject");
        certGenerator.setSubjectDN(subject);

        // Configurar el número de serie y las fechas de validez del certificado
        certGenerator.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGenerator.setNotBefore(new Date());
        certGenerator.setNotAfter(new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000)); // Validez de 1 año

        // Configurar la clave pública y la firma del certificado
        certGenerator.setPublicKey(keyPair.getPublic());
        certGenerator.setSignatureAlgorithm("SHA256WithRSAEncryption");

        // Generar el certificado
        X509Certificate cert = certGenerator.generate(keyPair.getPrivate(), "BC");

        return cert;
    }

    public static void saveCertificateToFile(Certificate cert, String filePath) throws CertificateException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(cert.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


