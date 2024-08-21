package zafus.rubikbmt.rubikbmt_website.services;

import jakarta.validation.constraints.Null;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ExcelExportService {

    public void exportCandidatesToExcel(List<Candidate> candidates, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh Sach");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = { "Họ và tên đệm", "Tên", "Ngày sinh", "Số điện thoại", "Email", "Cuộc thi", "Thời gian đăng ký", "Xác nhận" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Fill data rows
        int rowNum = 1;
        for (Candidate candidate : candidates) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(candidate.getLastName());
            row.createCell(1).setCellValue(candidate.getFirstName());
            row.createCell(2).setCellValue(candidate.getDateOfBirth() != null ? candidate.getDateOfBirth().toString() : "NULL");
            row.createCell(3).setCellValue(candidate.getPhoneNumber());
            row.createCell(4).setCellValue(candidate.getEmail());
            row.createCell(5).setCellValue(candidate.getCompetition() != null ? candidate.getCompetition().getName() : "NULL");
            row.createCell(6).setCellValue(candidate.getRegistrationTime() != null ? candidate.getRegistrationTime().toString() : "NULL");
            row.createCell(7).setCellValue(candidate.isConfirmed());
        }

        // Resize columns to fit the content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to the provided OutputStream
        workbook.write(outputStream);
        workbook.close();
    }
    public void exportStudentToExcel(List<Student> students, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh Sach");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = { "Họ và tên đệm", "Tên", "Ngày sinh", "Số điện thoại", "Email", "Tên Phụ Huynh", "Huấn Luyện Viên", "Ngày đăng ký","Ngày xác nhận","Hình Thức Học","Ghi chú" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Fill data rows
        int rowNum = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getFirstName());
            row.createCell(1).setCellValue(student.getLastName());
            row.createCell(2).setCellValue(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "NULL");
            row.createCell(3).setCellValue(student.getPhoneNumber());
            row.createCell(4).setCellValue(student.getEmail());
            row.createCell(5).setCellValue(student.getParentName());
            row.createCell(6).setCellValue(student.getMentor().getLastName() + " " + student.getMentor().getFirstName());
            row.createCell(7).setCellValue(student.getRegistrationDate() != null ? student.getRegistrationDate().toString() : "");
            row.createCell(8).setCellValue(student.getConfirmationDate() != null ? student.getConfirmationDate().toString() : "Chưa xác nhận");
            row.createCell(9).setCellValue(student.getLearningType().getLearningType());
            row.createCell(10).setCellValue(student.getNote());
        }

        // Resize columns to fit the content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to the provided OutputStream
        workbook.write(outputStream);
        workbook.close();
    }
}
