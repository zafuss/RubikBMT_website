package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.RegisterStudent;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.ExcelExportService;
import zafus.rubikbmt.rubikbmt_website.services.RegisterStudentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private CandidateService candidateService;

    @Autowired
    private RegisterStudentService registerStudentService;
    //    @GetMapping("/exportCandidates")
//    public ResponseEntity<String> exportCandidates() {
//        // Default file path to Downloads folder
//        List<Candidate> candidates = candidateService.findAll();
//        String defaultPath = System.getProperty("user.home") + "/Downloads/candidates.xlsx";
//
//        try {
//            excelExportService.exportCandidatesToExcel(candidates, defaultPath);
//            return ResponseEntity.ok("File exported successfully to " + defaultPath);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Error exporting file: " + e.getMessage());
//        }
//    }
    @GetMapping("/downloadCandidatesExcel")
    public ResponseEntity<Resource> downloadCandidatesExcel() {
        List<Candidate> candidates = candidateService.findAll();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Generate Excel file in memory using ByteArrayOutputStream
            excelExportService.exportCandidatesToExcel(candidates, out);

            // Create a ByteArrayResource from the ByteArrayOutputStream
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=thi_sinh_BackToSchool.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength()).body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/downloadStudentsExcel")
    public ResponseEntity<Resource> downloadStudentsExcel() {
        List<RegisterStudent> registerStudents = registerStudentService.findAll();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Generate Excel file in memory using ByteArrayOutputStream
            excelExportService.exportStudentToExcel(registerStudents, out);

            // Create a ByteArrayResource from the ByteArrayOutputStream
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=danh_sach_hocvien.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength()).body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/downloadCandidatesExcelByEvent")
    public ResponseEntity<Resource> downloadCandidatesExcelByEvent(@RequestParam("eventName") String eventName) {
        // Thiết lập tiêu đề cho file Excel

        // Lấy danh sách Candidate theo sự kiện
        List<Candidate> candidates = candidateService.getCandidatesByEventName(eventName);

        // Gọi hàm xuất Excel (giống như bạn đã làm cho export tổng quát)
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Generate Excel file in memory using ByteArrayOutputStream
            excelExportService.exportCandidatesByEventToExcel(candidates, out, eventName);

            // Create a ByteArrayResource from the ByteArrayOutputStream
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=thi_sinh_BackToSchool.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength()).body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }


}
