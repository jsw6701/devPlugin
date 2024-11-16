package com.github.jsw6701.devplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;

public class GenerateDtoConversionAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        if (psiFile == null || project == null) {
            return;
        }

        String fileName = psiFile.getName();

        // DTO 파일인지 확인
        if (!fileName.endsWith("Dto.java")) {
            return;
        }

        String dtoClassName = fileName.replace(".java", "");
        String entityName = dtoClassName.replace("Dto", "");
        PsiDirectory directory = psiFile.getContainingDirectory();
        PsiFile entityFile = directory.findFile(entityName + ".java");

        if (entityFile == null) {
            return; // 엔티티 파일이 없으면 작업 중단
        }

        // 변환 메서드 생성
        WriteCommandAction.runWriteCommandAction(project, () -> {
            String conversionMethods = generateConversionMethods(dtoClassName, entityName, psiFile, entityFile);
            appendToFile(psiFile, conversionMethods, project);
        });
    }

    private String generateConversionMethods(String dtoClassName, String entityName, PsiFile dtoFile, PsiFile entityFile) {
        StringBuilder methodBuilder = new StringBuilder();

        // `from` 메서드
        methodBuilder.append("    public static ").append(dtoClassName).append(" from(")
                .append(entityName).append(" entity) {\n")
                .append("        return ").append(dtoClassName).append(".builder()\n");

        // 엔티티 필드 기반 DTO 빌더 메서드 추가
        extractFields(entityFile).forEach(field -> {
            methodBuilder.append("            .").append(field[1]).append("(entity.get")
                    .append(Character.toUpperCase(field[1].charAt(0)))
                    .append(field[1].substring(1)).append("())\n");
        });

        methodBuilder.append("            .build();\n");
        methodBuilder.append("    }\n\n");

        // `toEntity` 메서드
        methodBuilder.append("    public ").append(entityName).append(" toEntity() {\n")
                .append("        return new ").append(entityName).append("(\n");

        extractFields(entityFile).forEach(field -> {
            methodBuilder.append("            this.").append(field[1]).append(",\n");
        });

        // 마지막 쉼표 제거
        methodBuilder.setLength(methodBuilder.length() - 2);

        methodBuilder.append("\n        );\n");
        methodBuilder.append("    }\n");

        return methodBuilder.toString();
    }

    private void appendToFile(PsiFile file, String content, Project project) {
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        var document = psiDocumentManager.getDocument(file);

        if (document != null) {
            int insertPosition = document.getText().lastIndexOf("}");
            WriteCommandAction.runWriteCommandAction(project, () -> {
                document.insertString(insertPosition, "\n" + content + "\n");
                psiDocumentManager.commitDocument(document);
            });
        }
    }

    private List<String[]> extractFields(PsiFile psiFile) {
        List<String[]> fields = new ArrayList<>();
        String text = psiFile.getText();

        // 파일 내용을 줄 단위로 나누기
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("private")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String fieldType = parts[1]; // 필드 타입
                    String fieldName = parts[2].replace(";", ""); // 필드 이름
                    fields.add(new String[]{fieldType, fieldName});
                }
            }
        }

        return fields;
    }
}
