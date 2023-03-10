package com.example.jwpproject.controller;

import com.example.jwpproject.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;

    @GetMapping("/excelDown")
    public ResponseEntity getExcelDown(HttpServletResponse response , @RequestParam String fileName){

        excelService.excels(response,fileName);
        return null;
        //return ResponseEntity.ok(excelService.getExcelDown(response,fileName));
    }
}
