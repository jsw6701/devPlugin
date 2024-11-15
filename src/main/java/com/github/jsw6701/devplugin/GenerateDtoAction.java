package com.github.jsw6701.devplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import io.ktor.client.engine.java.Java;

public class GenerateDtoAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);

        if (element == null) {
            return;
        }

        PsiFile psiFile = (PsiFile) element.getContainingFile();
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        var document = psiDocumentManager.getDocument(psiFile);

        if (document != null) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                document.insertString(document.getTextLength(), "a");
                psiDocumentManager.commitDocument(document);
            });
        }
/*
        // Entity 클래스가 포함된 파일이 맞는지 확인
        if (!isEntityFile(psiFile)) {
            return;
        }
*/

        // DTO 생성 로직 호출
//        DtoGenerator dtoGenerator = new DtoGenerator();
//        dtoGenerator.generateDto(psiFile, project);

/*        Runnable r = () -> EditorModificationUtil.insertStringAtCaret(editor, "a");

        // Run the modification inside a write command action
        WriteCommandAction.runWriteCommandAction(project, r);*/
    }

    private boolean isEntityFile(PsiFile file) {
        // 엔티티 클래스를 구별하는 로직 (예: @Entity 어노테이션 확인)
        return file.getText().contains("@Entity");
    }


}
