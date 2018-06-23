package footballstats;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ARFFBuilder {
    
	public Map<String, ArrayList<String>> instanceMap;
	public int highestLength;
	public String input;
	boolean inputUnknownValues;
        
	public ARFFBuilder (String inputFile, boolean inputUnknownValues) {
		this.instanceMap = new HashMap<String, ArrayList<String>>();
		this.highestLength = 0;
		this.input = inputFile;
		this.inputUnknownValues = inputUnknownValues;

	}
        
	/*public static void main(String[] args) {
            String action = "action";
            String logDate = "06052018";
            String filePath = "../../action_times/" + logDate + "/" + action + "_output.txt";
            filePath = action + "_output.txt";
            try {
                ARFFBuilder b = new ARFFBuilder(filePath, false);
                b.createArffFile();
            }
            catch (IOException e){}
	}*/

	public int getHighestLength() {
		return highestLength;
	}

	public void setHighestLength(int highestLength) {
		this.highestLength = highestLength;
	}

	public Map<String, ArrayList<String>> getInstanceMap() {
		return instanceMap;
	}

	public void setInstanceMap(Map<String, ArrayList<String>> instanceMap) {
		this.instanceMap = instanceMap;
	}

	public String actionFromString(String text) {
		if(text.toLowerCase().contains("pass")) {
			return "pass";
		}
		if(text.toLowerCase().contains("tackle")) {
			return "tackle";
		}
		if(text.toLowerCase().contains("dribble")) {
			return "dribble";
		}
		if(text.toLowerCase().contains("inaccuratep")) {
			return "inaccuratePass";
		}
		else return "";
	}

	public void createArffFile() throws IOException{
		readInputFile();
		writeToFile(getInstanceMap(), arffBeginning(getHighestLength()));

	}

        public String arffBeginning(int noAttributes){
		String beginning = "% 1. Title: Football Action Database\n%\n";
		beginning =  beginning +"% 2. Sources: \n%      (a) Creator: A.Joseph & N. Francis\n%\n";
		beginning =  beginning +"@RELATION Action";

        for (int i = 0; i < noAttributes; i++){
			beginning =  beginning + "\n@ATTRIBUTE position" + i + " NUMERIC";
		}

		beginning =  beginning +"\n@ATTRIBUTE class        {pass,tackle,dribble,inaccuratePass}\n@data\n";
				return beginning;
	}

	public void readInputFile() throws IOException {


		String line = "";
		String previousLine = "";
		BufferedReader br = new BufferedReader(new FileReader(input));
		while ((line = br.readLine()) != null) {
			// process the line.
			if (line.length() > 0) {
				String firstLetter = line.substring(0, 1);
				if (firstLetter.equals("[")) {
					line = line.replace("]", "");
					line = line.replace("[", "");
					line = line.replace("\n", "");
					int length = line.split(",").length;
					if (length > getHighestLength()) {
						setHighestLength(length);
					}
					previousLine = actionFromString(previousLine);
					if (getInstanceMap().get(previousLine) != null) {
						getInstanceMap().get(previousLine).add(line);
					} else {
						ArrayList<String> aList = new ArrayList<String>();
						getInstanceMap().put(previousLine, aList);
						getInstanceMap().get(previousLine).add(line);
					}
				}
				previousLine = line;
			} else {
				previousLine = line;
			}


		}
	}

	public void  writeToFile( Map<String, ArrayList<String>> map, String beginning) throws IOException{
		FileWriter fw = new FileWriter(input + "_rtlsTrain.arff", true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println(beginning);
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			@SuppressWarnings("unchecked")
			ArrayList<String> actionList = (ArrayList<String>) pair.getValue();
			String action = (String) pair.getKey();
			for (String positions : actionList) {
				String output = "";
				String [] lineArray = positions.split(",");
				int count = 0;
				//output = output + "\n";
				//output = output +"<";
				boolean beginAddingLastKnownPos = false;
				for (int i = 0; i < getHighestLength(); i++) {
					if(count >= lineArray.length) {
						if (!inputUnknownValues) {
							output = output + lineArray[lineArray.length - 3] + ",";
							output = output + lineArray[lineArray.length - 2] + ",";
							output = output + lineArray[lineArray.length - 1];
							count += 3;
							i += 2;
						}
						else {
							output = output + "?";
							count++;
						}
					}
					else {
						output = output + lineArray[i];
						count++;
					}
					output = output +",";

				}
				output = output + action;
				//out.println(action + "\n");

				out.println(output);


			}
		}


		out.close();
            System.out.println("done");
	}

}
