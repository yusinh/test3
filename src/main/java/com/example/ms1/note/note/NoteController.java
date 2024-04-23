package com.example.ms1.note.note;

import com.example.ms1.notebook.Notebook;
import com.example.ms1.notebook.NotebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{notebookId}/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final NotebookRepository notebookRepository;
    private final NoteService noteService;

    @PostMapping("/write")
    public String write(@PathVariable("notebookId") Long notebookId) {

        Notebook notebook = notebookRepository.findById(notebookId).orElseThrow();

        noteService.saveDefault(notebook);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("id") Long id, @PathVariable("notebookId") Long notebookId) {
        Note note = noteRepository.findById(id).get();
        List<Notebook> notebookList = notebookRepository.findAll();
        Notebook targetNotebook = notebookRepository.findById(notebookId).orElseThrow();

        List<Note> noteList = noteRepository.findByNotebook(targetNotebook);

        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNotebook", targetNotebook);
        model.addAttribute("notebookList", notebookList);

        return "main";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @PathVariable("notebookId") Long notebookId, String title, String content) {
        if(title.trim().length()==0) {
            title = "제목 없음";
        }

        Note note = noteRepository.findById(id).get();
        note.setTitle(title);
        note.setContent(content);

        noteRepository.save(note);
        return "redirect:/books/%d/notes/%d".formatted(notebookId, id);
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, @PathVariable("notebookId") Long notebookId) {
        noteRepository.deleteById(id);

        return "redirect:/";
    }
}
