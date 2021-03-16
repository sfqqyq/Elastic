package com.itsqq.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 基于前面Lcuene建立的索引库来进行全文检索
 * 根据关键字进行检索
 */
public class KeywordSearch {
    public static void main(String[] args) throws IOException {
        // 1、使用DirectoryReader.open 构建索引读取器
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("/Users/mac/IdeaProjects/Elastic/lucene_op/index")));

        // 2、构建索引查询器（IndexSearcher）
        // 用来搜索关键字的
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        // 3、构建词条（Term）和词条查询（TermQuery）（如果不使用分词器只能查询一个字，词语查不了）
//        TermQuery termQuery = new TermQuery(new Term("content", "跳"));
        // 3、使用IK分词器后可以使用词语查询
        TermQuery termQuery = new TermQuery(new Term("content", "人生"));
        // 3、使用句子
//        TermQuery termQuery = new TermQuery(new Term("content", "人生是一条河"));

        // 4、执行查询，获取文档
        TopDocs topDocs = indexSearcher.search(termQuery, 50);

        // 5、遍历打印文档（可以使用IndexSearch.doc根据文档id获取文档）
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // Lucene中，每一个文档都有一个唯一ID
            // 根据唯一ID就可以获取到文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            // 获取文档中的字段
            System.out.println("-------------");
            System.out.println("文件名："+doc.get("file_name"));
            System.out.println("文件路径："+doc.get("path"));
            System.out.println("文件内容："+doc.get("content"));
        }

        // 6、关闭索引读取器
        reader.close();

    }

}
