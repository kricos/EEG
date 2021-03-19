import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class ReadJsonAsString {
	Gson convertidor = new Gson();
	String pathJsonResult = "C:\\MMAD";
	
	public static void main(String[] args) throws Exception {
		ReadJsonAsString nuevo = new ReadJsonAsString();
		nuevo.loadUserList("C:\\MMAD\\Subjects");
		System.out.println("Ejecucion terminada");
    }
	
	
	private void loadUserList(String path) {
		String file = path +"\\userList.json";
		String json=null;
		try {
			 json = "{\"userList\":"+readFileAsString(file)+"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		WrapperUsersList data =  convertidor.fromJson(json, WrapperUsersList.class);
		ArrayList<UsersList> userlist = data.getUserList();
		for( UsersList user : userlist ) {
			System.out.println("Usuario Path:"+user.getDirPath());
			loadTestList(user.getDirPath());
		}
	}
	
	private void loadTestList(String path) {
		String file = path +"\\testList.json";
		String json=null;
		try {
			 json = "{\"testList\":"+readFileAsString(file)+"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		WrapperTestList data =  convertidor.fromJson(json, WrapperTestList.class);
		ArrayList<TestList> testList = data.getTestList();
		for( TestList test : testList ) {
			System.out.println("\tTest Path:"+test.getTestDir());
			loadRecordingList(test.getTestDir());
		}
	}
	
	private void loadRecordingList(String path) {
		String file = path +"\\recordingList.json";
		String json=null;
		try {
			 json = "{\"recordingList\":"+readFileAsString(file)+"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		WrapperRecordingList data =  convertidor.fromJson(json, WrapperRecordingList.class);
		ArrayList<RecordingList> recordingList = data.getRecordingList();
		for( RecordingList record : recordingList ) {
			System.out.println("\tAnotationsPath Path:"+record.getDataDir());
			loadAnotations(record.getDataDir());
		}
	}
	
	private void loadAnotations(String path) {
		// Archivo Base
        String file = path+"\\annotations.json";
        // Archivo Cambios
        String file2 = path+"\\annotations2.json";
        String json = null;
        String json2 = null;
        try {
			json = "{\"anotaciones\":"+readFileAsString(file)+"}";
			json2 = "{\"anotaciones\":"+readFileAsString(file2)+"}";
	        
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        // estas son las listas completas de cada prueba 
        ArrayList <CleanFixation> cleanFixationfull = new ArrayList<>();
    	ArrayList <EEGClass> EEGfull = new ArrayList<>();
    	ArrayList <GazeDataClass> GazeDatafull = new ArrayList<>();
        
        // Busca los archvos y directorios --------------------------------------------------------
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String pathToCsv = "";
        String row;
        boolean isHeader = true;
        BufferedReader csvReader;
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
        	pathToCsv = path+"\\"+listOfFiles[i].getName();
          	if(listOfFiles[i].getName().contains("EEGData.csv")) {
//                System.out.println("File EEF: " + listOfFiles[i].getName());
            	// antes de guardar se necesita cargar los archivos de csv
            	try {
	      			csvReader = new BufferedReader(new FileReader(pathToCsv));
	      			isHeader = true;
	      			while ((row = csvReader.readLine()) != null) {
	      				if(!isHeader) {
		      	            String[] fields = row.split(",");
		      	            EEGClass auxeeg = new EEGClass();
//		      	            System.out.println(row);
		      	            auxeeg.setC1( Float.valueOf( fields[0] ) );
			      	        auxeeg.setC2( Float.valueOf( fields[1] ) );
			      	        auxeeg.setC3( Float.valueOf( fields[2] ) );
			      	        auxeeg.setC4( Float.valueOf( fields[3] ) );
			      	        auxeeg.setC5( Float.valueOf( fields[4] ) );
			      	        auxeeg.setC6( Float.valueOf( fields[5] ) );
			      	        auxeeg.setC7( Float.valueOf( fields[6] ) );
			      	        auxeeg.setC8( Float.valueOf( fields[7] ) );
			      	        auxeeg.setTimestamp( Float.valueOf( fields[8] )  );
			      	        auxeeg.setAdjustedUnix( Float.valueOf( fields[9] )  );
			      	        EEGfull.add(auxeeg);
	      				}else {
	      					isHeader=false;
	      				}
		      	    }
	      	        csvReader.close();
	      		} catch (IOException e1) {
	      			// TODO Auto-generated catch block
	      			e1.printStackTrace();
	      		}
            }else
            if(listOfFiles[i].getName().contains("FixationData.csv")) {
//                System.out.println("File Fixation: " + listOfFiles[i].getName());
            	try {
	      			csvReader = new BufferedReader(new FileReader(pathToCsv));
	      			isHeader = true;
	      			while ((row = csvReader.readLine()) != null) {
	      				if( !isHeader ) {
	      					String[] fields = row.split(",");
		      	            CleanFixation auxfix = new CleanFixation();
		      	            auxfix.setStream( fields[0] );
		      	            auxfix.setX( Float.valueOf( fields[1] ) );
			      	        auxfix.setY( Float.valueOf( fields[2] ) );
			      	        auxfix.setLSLTimestamp( Float.valueOf( fields[3] ) );
			      	        auxfix.setAdjustedUnix( Float.valueOf( fields[4] ) );
			      	        cleanFixationfull.add(auxfix);
	      				}else {
	      					isHeader=false;
	      				}
		      	    }
	      	        csvReader.close();
	      		} catch (IOException e1) {
	      			// TODO Auto-generated catch block
	      			e1.printStackTrace();
	      		}
            }else
            if(listOfFiles[i].getName().contains("GazeData.csv")) {
//                System.out.println("File Gaze: " + listOfFiles[i].getName());
            	try {
	      			csvReader = new BufferedReader(new FileReader(pathToCsv));
	      			isHeader = true;
	      			while ((row = csvReader.readLine()) != null) {
	      				if( !isHeader ) {
	      					String[] fields = row.split(",");
		      	            GazeDataClass auxgaze = new GazeDataClass();
		      	            auxgaze.setX( Float.valueOf( fields[0] ) );
				      	    auxgaze.setY( Float.valueOf( fields[1] ) );
				      	    auxgaze.setDeviceTimestamp( Float.valueOf( fields[2] ) );
				      	    auxgaze.setLSLTimestamp( Float.valueOf( fields[3] ) );
				      	    auxgaze.setAdjustedUnix( Float.valueOf( fields[4] ) );
				      	    GazeDatafull.add(auxgaze);
	      				}else {
	      					isHeader=false;
	      				}
	      	        }
	      	        csvReader.close();
	      		} catch (IOException e1) {
	      			// TODO Auto-generated catch block
	      			e1.printStackTrace();
	      		}
            }
          } else if (listOfFiles[i].isDirectory()) {
//            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
        
    	
        // --------------------------------------------------------
		WrapperAnnotations data =  convertidor.fromJson(json, WrapperAnnotations.class);
        WrapperAnnotations data2 =  convertidor.fromJson(json2, WrapperAnnotations.class);
        for( int index = 0;  index < data.getAnotaciones().size() ; index ++) {
        	Annotations principal = data.getAnotaciones().get(index);
        	Annotations aReemplazar = data2.getAnotaciones().get(index);
        	principal.setInterest(aReemplazar.getInterest());
        	principal.setAttentiveness(aReemplazar.getAttentiveness());
        	principal.setEffort(aReemplazar.getEffort());
        	principal.setTimeRangeStart(aReemplazar.getTimeRangeStart());
        	principal.setTimeRangeEnd(aReemplazar.getTimeRangeEnd());
//			Aisgnar sublista con filtro de timestamp
//        	ArrayList <CleanFixation> cleanFixationfull = new ArrayList<>();
//        	ArrayList <EEGClass> EEGfull = new ArrayList<>();
//        	ArrayList <GazeDataClass> GazeDatafull = new ArrayList<>();
        	ArrayList <CleanFixation> cleanFixationaux = new ArrayList<>();
        	ArrayList <EEGClass> EEGaux = new ArrayList<>();
        	ArrayList <GazeDataClass> GazeDataaux = new ArrayList<>();
        	boolean isSession = false;
        	for(  CleanFixation objfixAux : cleanFixationfull ) {
        		if( objfixAux.getLSLTimestamp() <= principal.getTimeRangeEnd() && 
        				objfixAux.getLSLTimestamp() >= principal.getTimeRangeStart() 
        			||	isSession) {
        			if( objfixAux.getStream().contentEquals("Begin") ) {
        				isSession = true;
        			}
        			if( isSession ) {
        				cleanFixationaux.add(objfixAux);
        			}
        			if( objfixAux.getStream().contentEquals("End") ) {
        				isSession = false;
        			}
        		}
        	}
        	for(  EEGClass objEegAux : EEGfull ) {
        		if( objEegAux.getTimestamp() <= principal.getTimeRangeEnd() && 
        				objEegAux.getTimestamp() >= principal.getTimeRangeStart()	) {
        			EEGaux.add(objEegAux);
        		}
        	}
        	for(  GazeDataClass objGazeAux : GazeDatafull ) {
        		if( objGazeAux.getLSLTimestamp() <= principal.getTimeRangeEnd() && 
        				objGazeAux.getLSLTimestamp() >= principal.getTimeRangeStart()	) {
        			GazeDataaux.add(objGazeAux);
        		}
        	}
        	principal.setCleanFixation(cleanFixationaux);
        	principal.setEEG(EEGaux);
        	principal.setGazeData(GazeDataaux);
//        	System.out.println("Objeto en el index "+index+" con el json "+data.getAnotaciones().get(index));
        	try (FileWriter writer = new FileWriter(pathJsonResult+"\\"+principal.getUser()+principal.getTest()+getNameReduct(principal.getName())+".json")) {
        		convertidor.toJson(principal, writer);
        		//
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	
	private static String getNameReduct(String name) {
		name = name.trim();
		Pattern pattern = Pattern.compile("(.)[ \\w]+(\\d)");
	    Matcher matcher = pattern.matcher(name);
	    if( matcher.find() ) {
	    	name = matcher.group(1)+matcher.group(2);
	    }
		return name;
	}
	
    public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
    
    
    class WrapperRecordingList{
    	private ArrayList<RecordingList> recordingList;

		public ArrayList<RecordingList> getRecordingList() {
			return recordingList;
		}

		public void setRecordingList(ArrayList<RecordingList> recordingList) {
			this.recordingList = recordingList;
		}

		
    }
    class WrapperTestList{
    	private ArrayList<TestList> testList;

		public ArrayList<TestList> getTestList() {
			return testList;
		}

		public void setTestList(ArrayList<TestList> testList) {
			this.testList = testList;
		}
    }
    class WrapperUsersList{
    	private ArrayList<UsersList> userList;

		
		@Override
		public String toString() {
			return "anotaciones:"+userList;
		}


		public ArrayList<UsersList> getUserList() {
			return userList;
		}


		public void setUserlist(ArrayList<UsersList> userList) {
			this.userList = userList;
		}
    }
    
    class WrapperAnnotations{
    	private ArrayList<Annotations> anotaciones;

		public ArrayList<Annotations> getAnotaciones() {
			return anotaciones;
		}

		public void setAnotaciones(ArrayList<Annotations> anotaciones) {
			this.anotaciones = anotaciones;
		}
		@Override
		public String toString() {
			return "anotaciones:"+anotaciones;
		}
    }
    
    class RecordingList{
    	private boolean isPaper;
    	private boolean isCalibrated;
    	private String dataDir;
    	private String userID;
    	private String testName;
    	private float videoQpcStartTime;
    	private float videoQpcEndTime;
    	private int Index;
    	@Override
    	public String toString() {
    		return "isPaper:"+isPaper+"\n"+
    				"isCalibrated:"+isCalibrated+"\n"+
    				"dataDir:"+dataDir+"\n"+
    				"userID:"+userID+"\n"+
    				"testName:"+testName+"\n"+
    				"videoQpcStartTime:"+videoQpcStartTime+"\n"+
    				"videoQpcEndTime:"+videoQpcEndTime+"\n"+
    				"Index:"+Index+"\n";
    	}
		public boolean isPaper() {
			return isPaper;
		}
		public void setPaper(boolean isPaper) {
			this.isPaper = isPaper;
		}
		public boolean isCalibrated() {
			return isCalibrated;
		}
		public void setCalibrated(boolean isCalibrated) {
			this.isCalibrated = isCalibrated;
		}
		public String getDataDir() {
			return dataDir;
		}
		public void setDataDir(String dataDir) {
			this.dataDir = dataDir;
		}
		public String getUserID() {
			return userID;
		}
		public void setUserID(String userID) {
			this.userID = userID;
		}
		public String getTestName() {
			return testName;
		}
		public void setTestName(String testName) {
			this.testName = testName;
		}
		public float getVideoQpcStartTime() {
			return videoQpcStartTime;
		}
		public void setVideoQpcStartTime(float videoQpcStartTime) {
			this.videoQpcStartTime = videoQpcStartTime;
		}
		public float getVideoQpcEndTime() {
			return videoQpcEndTime;
		}
		public void setVideoQpcEndTime(float videoQpcEndTime) {
			this.videoQpcEndTime = videoQpcEndTime;
		}
		public int getIndex() {
			return Index;
		}
		public void setIndex(int index) {
			Index = index;
		}
    }
    
    class TestList{
    	private boolean isPaper;
    	private int index;
    	private String Name;
    	private String StimuliPath;
    	private String UserID;
    	private String TestDir;
    	private int numRecordings;
    	@Override
    	public String toString() {
    		return "isPaper:"+isPaper+"\n"+
    				"index:"+index+"\n"+
    				"Name:"+Name+"\n"+
    				"StimuliPath:"+StimuliPath+"\n"+
    				"UserID:"+UserID+"\n"+
    				"TestDir:"+TestDir+"\n"+
    				"numRecordings:"+numRecordings+"\n";
    	}
		public boolean isPaper() {
			return isPaper;
		}
		public void setPaper(boolean isPaper) {
			this.isPaper = isPaper;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public String getStimuliPath() {
			return StimuliPath;
		}
		public void setStimuliPath(String stimuliPath) {
			StimuliPath = stimuliPath;
		}
		public String getUserID() {
			return UserID;
		}
		public void setUserID(String userID) {
			UserID = userID;
		}
		public String getTestDir() {
			return TestDir;
		}
		public void setTestDir(String testDir) {
			TestDir = testDir;
		}
		public int getNumRecordings() {
			return numRecordings;
		}
		public void setNumRecordings(int numRecordings) {
			this.numRecordings = numRecordings;
		}
    }
    
    
    
    class UsersList{
    	private int highestTestIndex;
    	private String Id;
    	private String GroupName;
    	private String GroupPath;
    	private String DirPath;
    	@Override
    	public String toString() {
    		return "highestTestIndex:"+highestTestIndex+"\n"+
    				"Id:"+Id+"\n"+
    				"GroupName:"+GroupName+"\n"+
    				"GroupPath:"+GroupPath+"\n"+
    				"DirPath:"+DirPath+"\n";
    	}
    	
		public int getHighestTestIndex() {
			return highestTestIndex;
		}
		public void setHighestTestIndex(int highestTestIndex) {
			this.highestTestIndex = highestTestIndex;
		}
		public String getId() {
			return Id;
		}
		public void setId(String id) {
			Id = id;
		}
		public String getGroupName() {
			return GroupName;
		}
		public void setGroupName(String groupName) {
			GroupName = groupName;
		}
		public String getGroupPath() {
			return GroupPath;
		}
		public void setGroupPath(String groupPath) {
			GroupPath = groupPath;
		}
		public String getDirPath() {
			return DirPath;
		}
		public void setDirPath(String dirPath) {
			DirPath = dirPath;
		}
    	
    }
    
    
    
    
    class Annotations {
    	private String user;
    	private String test;
    	private ArrayList<String> points;
    	private String Name;
    	private int Interest;
    	private int Attentiveness;
    	private int Effort;
    	private float timeRangeStart;
    	private float timeRangeEnd;
    	private ArrayList <CleanFixation> cleanFixation;
    	private ArrayList <EEGClass> EEG;
    	private ArrayList <GazeDataClass> GazeData;
    	@Override
    	public String toString() {
    		return "user:"+user+"\n"+
    				"test:"+test+"\n"+
    				"points:"+points+"\n"+
    				"Name:"+Name+"\n"+
    				"Interest:"+Interest+"\n"+
    				"Attentiveness:"+Attentiveness+"\n"+
    				"Effort:"+Effort+"\n"+
    				"timeRangeStart:"+timeRangeStart+"\n"+
    				"timeRangeEnd:"+timeRangeEnd+"\n";
    	}
    	
    	
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getTest() {
			return test;
		}
		public void setTest(String test) {
			this.test = test;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public ArrayList<String> getPoints() {
			return points;
		}
		public void setPoints(ArrayList<String> points) {
			this.points = points;
		}
		public float getTimeRangeStart() {
			return timeRangeStart;
		}


		public void setTimeRangeStart(float timeRangeStart) {
			this.timeRangeStart = timeRangeStart;
		}


		public float getTimeRangeEnd() {
			return timeRangeEnd;
		}


		public void setTimeRangeEnd(float timeRangeEnd) {
			this.timeRangeEnd = timeRangeEnd;
		}


		public int getAttentiveness() {
			return Attentiveness;
		}


		public void setAttentiveness(int attentiveness) {
			Attentiveness = attentiveness;
		}


		public int getEffort() {
			return Effort;
		}


		public void setEffort(int effort) {
			Effort = effort;
		}


		public int getInterest() {
			return Interest;
		}


		public void setInterest(int interest) {
			Interest = interest;
		}


		public ArrayList<CleanFixation> getCleanFixation() {
			return cleanFixation;
		}


		public void setCleanFixation(ArrayList<CleanFixation> cleanFixation) {
			this.cleanFixation = cleanFixation;
		}


		public ArrayList<EEGClass> getEEG() {
			return EEG;
		}


		public void setEEG(ArrayList<EEGClass> eEG) {
			EEG = eEG;
		}


		public ArrayList<GazeDataClass> getGazeData() {
			return GazeData;
		}


		public void setGazeData(ArrayList<GazeDataClass> gazeData) {
			GazeData = gazeData;
		}
    }
    
    class CleanFixation {
    	private String Stream;
    	private float X;
    	private float Y;
    	private float LSLTimestamp;
    	private float AdjustedUnix;
    	@Override
    	public String toString() {
    		return "Stream:"+Stream+"\n"+
    				"X:"+X+"\n"+
    				"Y:"+Y+"\n"+
    				"LSLTimestamp:"+LSLTimestamp+"\n"+
    				"AdjustedUnix:"+AdjustedUnix+"\n"
    				;
    	}
		public String getStream() {
			return Stream;
		}
		public void setStream(String stream) {
			Stream = stream;
		}
		public float getX() {
			return X;
		}
		public void setX(float x) {
			X = x;
		}
		public float getY() {
			return Y;
		}
		public void setY(float y) {
			Y = y;
		}
		public float getLSLTimestamp() {
			return LSLTimestamp;
		}
		public void setLSLTimestamp(float lSLTimestamp) {
			LSLTimestamp = lSLTimestamp;
		}
		public float getAdjustedUnix() {
			return AdjustedUnix;
		}
		public void setAdjustedUnix(float adjustedUnix) {
			AdjustedUnix = adjustedUnix;
		}
    }
    
    class EEGClass {
    	private float C1;
    	private float C2;
    	private float C3;
    	private float C4;
    	private float C5;
    	private float C6;
    	private float C7;
    	private float C8;
    	private float Timestamp;
    	private float AdjustedUnix;
    	@Override
    	public String toString() {
    		return "C1:"+C1+"\n"+
    				"C2:"+C2+"\n"+
    				"C3:"+C3+"\n"+
    				"C4:"+C4+"\n"+
    				"C5:"+C5+"\n"+
    				"C6:"+C6+"\n"+
    				"C7:"+C7+"\n"+
    				"C8:"+C8+"\n"+
    				"Timestamp:"+Timestamp+"\n"+
    				"AdjustedUnix:"+AdjustedUnix+"\n"
    				;
    	}
		public float getC1() {
			return C1;
		}
		public void setC1(float c1) {
			C1 = c1;
		}
		public float getC2() {
			return C2;
		}
		public void setC2(float c2) {
			C2 = c2;
		}
		public float getC3() {
			return C3;
		}
		public void setC3(float c3) {
			C3 = c3;
		}
		public float getC4() {
			return C4;
		}
		public void setC4(float c4) {
			C4 = c4;
		}
		public float getC5() {
			return C5;
		}
		public void setC5(float c5) {
			C5 = c5;
		}
		public float getC6() {
			return C6;
		}
		public void setC6(float c6) {
			C6 = c6;
		}
		public float getC7() {
			return C7;
		}
		public void setC7(float c7) {
			C7 = c7;
		}
		public float getC8() {
			return C8;
		}
		public void setC8(float c8) {
			C8 = c8;
		}
		public float getTimestamp() {
			return Timestamp;
		}
		public void setTimestamp(float timestamp) {
			Timestamp = timestamp;
		}
		public float getAdjustedUnix() {
			return AdjustedUnix;
		}
		public void setAdjustedUnix(float adjustedUnix) {
			AdjustedUnix = adjustedUnix;
		}
    }

	class GazeDataClass {
		private float X;
		private float Y;
		private float DeviceTimestamp;
		private float LSLTimestamp;
		private float AdjustedUnix;
		@Override
		public String toString() {
			return "X:"+X+"\n"+
					"Y:"+Y+"\n"+
					"DeviceTimestamp:"+DeviceTimestamp+"\n"+
					"LSLTimestamp:"+LSLTimestamp+"\n"+
					"AdjustedUnix:"+AdjustedUnix+"\n";
		}
		public float getX() {
			return X;
		}
		public void setX(float x) {
			X = x;
		}
		public float getY() {
			return Y;
		}
		public void setY(float y) {
			Y = y;
		}
		public float getDeviceTimestamp() {
			return DeviceTimestamp;
		}
		public void setDeviceTimestamp(float deviceTimestamp) {
			DeviceTimestamp = deviceTimestamp;
		}
		public float getLSLTimestamp() {
			return LSLTimestamp;
		}
		public void setLSLTimestamp(float lSLTimestamp) {
			LSLTimestamp = lSLTimestamp;
		}
		public float getAdjustedUnix() {
			return AdjustedUnix;
		}
		public void setAdjustedUnix(float adjustedUnix) {
			AdjustedUnix = adjustedUnix;
		}
	}
}
