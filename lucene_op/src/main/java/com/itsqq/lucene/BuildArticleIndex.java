package com.itsqq.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 构建文件索引
 */
public class BuildArticleIndex {
    public static void main(String[] args) throws IOException {
        // 1、构建分词器（StandardAnalyzer）
//        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        // 1、使用IKA分词器
        IKAnalyzer standardAnalyzer = new IKAnalyzer();
        // 2、构建文档写入器配置（IndexWriterConfig）
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
        // 3、创建文档写入器(IndexWriter,注意：需要使用Paths来)
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(Paths.get("/Users/mac/IdeaProjects/Elastic/lucene_op/index")),indexWriterConfig);
        // 4、读取所有文件构建文档
        File dataDir = new File("/Users/mac/IdeaProjects/Elastic/lucene_op/data");
        File[] fileArray = dataDir.listFiles();
        // 迭代所有扽文本文件，读取文件并建立索引
        for (File file : fileArray) {
            // 5. 文档中添加字段
            // 字段名	类型	说明
            // file_name	TextFiled	文件名字段，需要在索引文档中保存文件名内容
            // content	TextFiled	内容字段，只需要能被检索，但无需在文档中保存
            // path	StoredFiled	路径字段，无需被检索，只需要在文档中保存即可

            // 在Lucene中都是以Document的形式来存储内容的
            // Lucene在添加文档的时候就会自动建立索引
            Document document = new Document();
            document.add(new TextField("file_name",file.getName(), Field.Store.YES));
            document.add(new TextField("content", FileUtils.readFileToString(file), Field.Store.NO));
            document.add(new StoredField("path",file.getAbsolutePath()));
            // 6、写入文档
            indexWriter.addDocument(document);
        }
        // 7、关闭写入器
        indexWriter.close();
    }


}
