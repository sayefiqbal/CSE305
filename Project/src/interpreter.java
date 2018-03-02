import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 
 * @author Sayef Iqbal
 *
 */

public class interpreter{
	private static Stack<String> _store = new Stack<String>();
	
	public static void interpreter(String input, String output){
		ArrayList<String> inputList = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String temp; 
			while((temp = reader.readLine()) != null){
				inputList.add(temp); 
			}			
			reader.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			return; 
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			for(int i=0; i<inputList.size(); ++i){
				interpret(inputList.get(i));
			}
			while(!_store.isEmpty()){
//				if(_store.peek()=="quit"){
//					return; 
//				}
				writer.write(_store.pop() + "\n");
			}
			writer.close();
			System.out.println("It worked");
			return;
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return; 
		} 
	}
	
	private static void interpret(String command){
		if(command.contains("quit")){
			return; 
		}
		if(!_store.isEmpty() && _store.peek()==":error:"){
			_store.push(":error:");
			return;
		}
		if(command.contains("push")){
			String numPush = command.substring(5,command.length());
			try{
				if(isInt(numPush)){
					if(numPush.equals("-0")){
						_store.push("0");
						return; 
					}else{
						_store.push(numPush);
						return; 
					}
				}else if (numPush.contains("\"")){
					_store.push(numPush.substring(1, numPush.length()-1));
					return; 
				}else{
					_store.push(numPush);
					return; 
				}
			}
			catch(IndexOutOfBoundsException ex){
				_store.push(":error:");
				return; 
			}
		}else if(command.contains("pop")){
			try{
				_store.pop();
				return; 
			}
			catch(EmptyStackException ex){
				_store.push(":error:");
				return; 
			}
		}
		else if(command.contains("mul")){
			
		}

	}
	
	public static boolean isInt(String s)throws IndexOutOfBoundsException, NumberFormatException{
	    try{
	        Double temp = Double.parseDouble(s);
	        if(temp - (int)(temp/1) == 0.0){
	        	return true; 
	        }
	        else{
	        	throw new IndexOutOfBoundsException(); 
	        }
	    } catch (NumberFormatException ex){
	    	return false; 
	    }
	}
	
	public static void main(String[] args){
		interpreter("input.txt", "output.txt");
	}
}
