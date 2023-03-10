package com.example.jwpproject.service;

import com.example.jwpproject.model.Member;
import com.example.jwpproject.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelService {
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    public Object getExcelDown(HttpServletResponse response,String fileName){

        List<Member> members = memberRepository.findAll();

        createExcel(response,members,fileName);

        List<Object> memberMap = members.stream()
                            .map(mem -> objectMapper.convertValue(mem, Member.class))
                            .collect(Collectors.toList());
        return memberMap;
    }

    public void excels(HttpServletResponse response,String fileName){

        List<Member> members = memberRepository.findAll();
        createExcel(response,members,fileName);
    }

    private void createExcel(HttpServletResponse response ,List<Member> members,String fileName){

        try{
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("사용자 포인트 통계");

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);

            cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {"아이디","계정","닉네임","이메일","권한"};

            Row row = sheet.createRow(0);

            int index = 0;
            for (String header : headers){
                Cell cell = row.createCell(index++);
                cell.setCellValue(header);
            }

            for(int i=0;i<members.size();i++){
                row = sheet.createRow(i+1);

                Member mem = members.get(i);

                Cell cell = null;
                cell = row.createCell(0);
                cell.setCellValue(mem.getId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(1);
                cell.setCellValue(mem.getAccount());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(2);
                cell.setCellValue(mem.getName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(3);
                cell.setCellValue(mem.getEmail());
                cell.setCellStyle(cellStyle);

            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xlsx");
            wb.write(response.getOutputStream());
            wb.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
