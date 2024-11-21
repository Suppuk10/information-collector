package com.avirantEnterprises.information_collector.controller.personal;

import com.avirantEnterprises.information_collector.service.personal.PdfService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @GetMapping("/profile/download/{id}")
    public ResponseEntity<byte[]> downloadUserProfilePdf(@PathVariable Long id) throws IOException, DocumentException {
        byte[] pdfContent = pdfService.generateUserProfilePdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "user_profile_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }
}
