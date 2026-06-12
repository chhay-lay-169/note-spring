package com.chhaylay.note_java.controller;

import com.chhaylay.note_java.dto.ApiResponse;
import com.chhaylay.note_java.dto.NoteDto;
import com.chhaylay.note_java.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ApiResponse<Page<NoteDto.ResponseAll>> getAllNotes(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ApiResponse.success("Success", noteService.getAllNotes(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<NoteDto.ResponseDetail> getNoteById(@PathVariable Long id) {
        return ApiResponse.success("Success", noteService.getNoteById(id));
    }

    @PostMapping
    public ApiResponse<NoteDto.ResponseDetail> createNote(@Valid @RequestBody NoteDto.Request request) {
        return ApiResponse.success("Success", noteService.createNote(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<NoteDto.ResponseDetail> updateNote(
            @PathVariable Long id, 
            @Valid @RequestBody NoteDto.Request request) {
        return ApiResponse.success("Success", noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ApiResponse.success("Success", null);
        // return ResponseEntity.noContent().build();
    }
}