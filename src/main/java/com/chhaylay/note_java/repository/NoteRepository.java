package com.chhaylay.note_java.repository;

import com.chhaylay.note_java.dto.NoteDto;
import com.chhaylay.note_java.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // Overriding standard paginated lookup to guarantee clean execution
    // @Query("SELECT new com.chhaylay.note_java.dto.NoteDto$ResponseAll(n.id, n.title, n.updatedAt) FROM Note n")
    Page<Note> findAll(Pageable pageable);
}