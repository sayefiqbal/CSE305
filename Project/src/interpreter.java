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
	
	public static void test(String input, String output){
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
//		if(!_store.isEmpty() && _store.peek()==":error:"){
//			_store.push(":error:");
//			return;
//		}
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
		else if(command.contains("add")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val1 + val2; 
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return; 
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return; 
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return; 
				}
			}
		}else if(command.contains("mul")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val1 * val2; 
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return; 
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return; 
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return; 
				}
			}
		}else if(command.contains("sub")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val2 - val1; 
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return; 
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return; 
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return; 
				}
			}
		}else if(command.contains("div")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						if(val1 == 0){
							_store.push(sVal2);
							_store.push(sVal1);
							_store.push(":error:");
							return;
						}
						int val2 = Integer.parseInt(sVal2);
						int val = val2 / val1; 
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("rem")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						if(val1 == 0){
							_store.push(sVal2);
							_store.push(sVal1);
							_store.push(":error:");
							return;
						}
						int val2 = Integer.parseInt(sVal2);
						int val = val2 % val1; 
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("neg")){
			if(_store.isEmpty()){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				try{
					if(isInt(sVal1)){
						int val1 = Integer.parseInt(sVal1);
						if(val1 == 0){
							return;
						}
						int val = -1 * val1; 
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("swap")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return; 
			}else{
				String sVal1 = _store.pop(); 
				String sVal2 = _store.pop();
				_store.push(sVal1);
				_store.push(sVal2);
			}
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
		test("input.txt", "output.txt");
	}
}
