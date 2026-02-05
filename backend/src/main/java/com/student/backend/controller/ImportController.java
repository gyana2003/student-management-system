package com.student.backend.controller;

import com.student.backend.service.ExcelImportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class ImportController {

    private final ExcelImportService excelImportService;

    public ImportController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping(value = "/attendance", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importAttendance(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(Map.of("error", "File must be in .xlsx format"));
        }

        Map<String, Object> result = excelImportService.importAttendanceFromExcel(file);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/marks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importMarks(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(Map.of("error", "File must be in .xlsx format"));
        }

        Map<String, Object> result = excelImportService.importMarksFromExcel(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attendance/template")
    public ResponseEntity<byte[]> downloadAttendanceTemplate() {
        byte[] template = excelImportService.generateAttendanceTemplate();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"attendance_template.xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(template);
    }

    @GetMapping("/marks/template")
    public ResponseEntity<byte[]> downloadMarksTemplate() {
        byte[] template = excelImportService.generateMarksTemplate();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"marks_template.xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(template);
    }
}
