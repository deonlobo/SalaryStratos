package com.analysis.SalaryStratos;

import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.features.SpellChecker;
import com.analysis.SalaryStratos.services.JobDataTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;

@SpringBootApplication
public class SalaryStratosApplication {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		ApplicationContext applicationContext = SpringApplication.run(SalaryStratosApplication.class, args);
		JobDataTrie jobData = applicationContext.getBean(JobDataTrie.class);
		SpellChecker checker = applicationContext.getBean(SpellChecker.class);

//		jobData.getJobsDataFromJson();
		TrieDS trie = jobData.initializeTrie();
		checker.initializeSpellChecker(trie);

	}

}
