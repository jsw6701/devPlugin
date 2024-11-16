package com.github.jsw6701.devplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

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

        // Entity 클래스가 포함된 파일이 맞는지 확인
        if (!isEntityFile(psiFile)) {
            return;
        }

        String entityName = psiFile.getName().replace(".java", "");
        String dtoFileName = entityName + "Dto.java";
        PsiDirectory directory = psiFile.getContainingDirectory();



        /*PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        var document = psiDocumentManager.getDocument(psiFile);

        if (document != null) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                document.insertString(document.getTextLength(), "a");
                psiDocumentManager.commitDocument(document);
            });
        }*/

        // DTO 파일 생성
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                PsiFile dtoFile = directory.findFile(dtoFileName);
                if (dtoFile != null) {
                    return; // 이미 파일이 존재하면 생성하지 않음
                }

                // 새 파일 생성
                String dtoContent = generateDtoContent(entityName, psiFile);
                PsiFile newFile = directory.createFile(dtoFileName);
                PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
                var document = psiDocumentManager.getDocument(newFile);

                if (document != null) {
                    document.setText(dtoContent); // DTO 파일 내용 추가
                    psiDocumentManager.commitDocument(document);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isEntityFile(PsiFile file) {
        // 엔티티 클래스를 구별하는 로직 (예: @Entity 어노테이션 확인)
        return file.getText().contains("@Entity");
    }
    private String generateDtoContent(String entityName, PsiFile psiFile) {
        // 엔티티 클래스의 내용을 기반으로 필드 생성
        StringBuilder dtoBuilder = new StringBuilder();
        dtoBuilder
                .append("import lombok.*;\n")
                .append("import java.util.*;\n")
                .append("import java.time.*;\n\n")
                .append("@Getter\n")
                .append("@Builder\n")
                .append("@NoArgsConstructor\n")
                .append("@AllArgsConstructor\n")
                .append("public class ").append(entityName).append("Dto {\n\n");

        String entityText = psiFile.getText();
        String[] lines = entityText.split("\n");

        // 엔티티 필드 파싱 및 DTO 필드 생성
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("private")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String fieldType = parts[1]; // 필드 타입
                    String fieldName = parts[2].replace(";", ""); // 필드 이름
                    dtoBuilder.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
                }
            }
        }

        dtoBuilder.append("\n}");
        return dtoBuilder.toString();
    }

}
