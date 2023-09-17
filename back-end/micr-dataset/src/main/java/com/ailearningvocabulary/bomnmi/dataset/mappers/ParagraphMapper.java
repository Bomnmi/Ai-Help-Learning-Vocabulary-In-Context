package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParagraphMapper {

    int insertBatchParagraphs(List<Paragraph> paragraphList);

    List<Paragraph> selectUnReadParagraphByUserId(@Param("userId") String userId);

    Paragraph selectParagraphByParagraphId(@Param("paragraphId") String paragraphId);

    int updateSelectiveParagraph(Paragraph paragraph);
}