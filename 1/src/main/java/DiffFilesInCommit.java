import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;



public class DiffFilesInCommit {
	

    public static void main(String[] args) throws Exception {

    	
    	PrintStream console = System.out; 
        List<File> Files = new ArrayList<>();
        String REMOTE_URL = "https://github.com/kRhythm/SampleCode.git";
        Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(new File("C:\\Users\\nvuggam\\Desktop\\DCA"))
                .call();

        try (Repository repository = result.getRepository()) {
            
            Collection<Ref> allRefs = repository.getAllRefs().values();

            try (RevWalk revWalk = new RevWalk( repository )) {
                for( Ref ref : allRefs ) {
                    revWalk.markStart( revWalk.parseCommit( ref.getObjectId() ));
                }
                System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
                int count = 0;
                RevCommit previouscommit = null;
                RevCommit presentcommit = null;
                for( RevCommit commit : revWalk ) 
                {
                    if(count==0) {
                        previouscommit = commit;
                        File f = new File("C:\\Users\\nvuggam\\Desktop\\commitmsg"+count+".txt");
                        PrintStream fhf = new PrintStream(f);
                        System.setOut(fhf);
                        String commitMessage = commit.getFullMessage();
                        StringTokenizer st2 =  
                                new StringTokenizer(commitMessage, " :,.-()"); 
                           while (st2.hasMoreTokens()) 
                               System.out.println(st2.nextToken());
                         System.setOut(console);
                    }
                    else 
                    {
                        presentcommit = commit;
                        File fi = new File("C:\\Users\\nvuggam\\Desktop\\commitmsg"+count+".txt");
                        PrintStream fhf = new PrintStream(fi);
                        System.setOut(fhf);
                        String commitMessage = commit.getFullMessage();
                        StringTokenizer st2 =  
                                new StringTokenizer(commitMessage, " :,.-(){}[]") ; 
                           while (st2.hasMoreTokens()) 
                               System.out.println(st2.nextToken());
                         System.setOut(console);
                        try (Git git = new Git(repository)) 
                        {
                            RevTree previoustree = previouscommit.getTree();
                            RevTree presenttree = presentcommit.getTree();
                            File FileInfo= new File("C:\\Users\\nvuggam\\Desktop\\FileInfo.txt");
                            PrintStream hi = new PrintStream(FileInfo);
                            System.setOut(hi);
                            listDiff(repository, git,presentcommit,previouscommit);
                            BufferedReader Fi = new BufferedReader(new FileReader(FileInfo));
                            String Fline = null;
                            ArrayList<String> OriginalM = new ArrayList<String>();
                            ArrayList<String> RevisedM = new ArrayList<String>();
                            
                            while(  (Fline = Fi.readLine()) != null )
                            {
                                String[] WordsOfLine = Fline.split(" ",3) ;
                                if(WordsOfLine[0].equals("MODIFY"))
                                {
                                    OriginalM.add(WordsOfLine[1]);
                                    RevisedM.add(WordsOfLine[2]);      
                                }
                                else if(WordsOfLine[0].contentEquals("ADD")) 
							 	{
								 File DifferenceFile = new
								 File("C:\\Users\\nvuggam\\Desktop\\Difference.txt"); 
								 PrintStream AF = new
								 PrintStream(DifferenceFile);
								 String D = WordsOfLine[2];
								 System.setOut(console);
	                                System.out.println("A"+D);
								 try (TreeWalk treeWalk = new TreeWalk(repository)) 
								 	{ 
									 treeWalk.addTree(previoustree);
									 treeWalk.setRecursive(true);
									 treeWalk.setFilter(PathFilter.create(D));
									 ObjectId objectId = treeWalk.getObjectId(0);
									 ObjectLoader loader =repository.open(objectId);
									 System.setOut(AF); 
									 loader.copyTo(System.out);
									 Tokenize t = new Tokenize(count); 
									 t.main(null); 
									 File f = new File("C:\\Users\\nvuggam\\Desktop\\tokens"+count+".txt");
		                             Files.add(f);
									 System.setOut(console); 
									 System.out.println("Had " + count + " commits");
								 	} 
								 }
							 	else if(WordsOfLine[0].equals("DELETE"))
							 	{ 
								  File DifferenceFile = new File("C:\\Users\\nvuggam\\Desktop\\Difference.txt"); 
								  PrintStream DF = new
								  PrintStream(DifferenceFile); 
								  String D = WordsOfLine[2]; 
								  System.setOut(console);
	                                System.out.println("D"+D);
								  try (TreeWalk treeWalk = new TreeWalk(repository)) 
								  {
								  treeWalk.addTree(presenttree);
								  treeWalk.setRecursive(true); 
								  treeWalk.setFilter(PathFilter.create(D));
								  ObjectId objectId = treeWalk.getObjectId(0);
								  ObjectLoader loader = repository.open(objectId);
								  System.setOut(DF); 
								  loader.copyTo(System.out);
								  }
							   Tokenize t = new Tokenize(count);
							   t.main(null); 
							   File f = new File("C:\\Users\\nvuggam\\Desktop\\tokens"+count+".txt");
                               Files.add(f);
							   System.setOut(console); 
							   System.out.println("Had " + count + " commits"); 
							 }	 
                            }
                            Fi.close();
                            for(int i=0;i<OriginalM.size();i++)
                            {
                                File OringinalFile = new File("C:\\Users\\nvuggam\\Desktop\\Original.java");
                                File RevisedFile = new File("C:\\Users\\nvuggam\\Desktop\\Revised.java");
                                PrintStream OF = new PrintStream(OringinalFile);
                                PrintStream RF = new PrintStream(RevisedFile);
                                String O = OriginalM.get(i);///call the cloud search
                                System.setOut(console);
                                System.out.println("M"+O);
                                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                                    treeWalk.addTree(presenttree);
                                    treeWalk.setRecursive(true);
                                    treeWalk.setFilter(PathFilter.create(O));
                                    ObjectId objectId = treeWalk.getObjectId(0);
                                    ObjectLoader loader = repository.open(objectId);
                                    System.setOut(OF);
                                    loader.copyTo(System.out);
                                }
                                String R = RevisedM.get(i);
                                System.setOut(console);
                                System.out.println("M"+R);
                                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                                    treeWalk.addTree(previoustree);
                                    treeWalk.setRecursive(true);
                                    treeWalk.setFilter(PathFilter.create(R));
                                    ObjectId objectId = treeWalk.getObjectId(0);
                                    ObjectLoader loader = repository.open(objectId);
                                    System.setOut(RF);
                                    loader.copyTo(System.out);
                                }
                                graph_diff g = new graph_diff(count);  
                                g.main(null);
                                File f = new File("C:\\Users\\nvuggam\\Desktop\\tokens"+count+".txt");
                                Files.add(f);
                                ///call the cloud search
                                System.setOut(console);
                                System.out.println("Had " + count + " commits");
                            }
                        }
                        previouscommit = presentcommit;
                    }
                    count++;
                } 
            }
        }
    }
    
    private static void listDiff(Repository repository, Git git, ObjectId oldCommit, ObjectId newCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diffs = git.diff()
                .setOldTree(prepareTreeParser(repository, oldCommit))
                .setNewTree(prepareTreeParser(repository, newCommit))
                .call();

        //System.out.println("Found: " + diffs.size() + " differences");
        for (DiffEntry diff : diffs) {
            System.out.println(diff.getChangeType() + " " + diff.getOldPath() + " " + diff.getNewPath() );
        }
    }
    
    private static AbstractTreeIterator prepareTreeParser(Repository repository, ObjectId objectId) throws IOException {

        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(objectId);
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }
    
    
}    
    
        
  