package com.acdlabs.task.gwt.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.google.gwt.user.client.Window;

public class Process {
	
	public void process(String[] str_from_ta, String[] for_working,int counter, ArrayList<String> result){
		try{
		//получаем матрицу элементов
		String[][] elem_from_ta = new String[for_working.length][];
        for (int i=0; i<for_working.length; i++){
            elem_from_ta[i] = for_working[i].split("\t");
        }
        //создаем временное хранилище
		ArrayList<String> temp = new ArrayList<String>();	
		//ищем макс элемент
		String max = elem_from_ta[0][counter];
		for(int i = 1; i < for_working.length; i++){
			//проверяем,число или нет
			boolean parsable_max = true;
		    try{
		        Double.parseDouble(max);
		    }catch(NumberFormatException e){
		        parsable_max = false;
		    }
		    boolean parsable_elem = true;
		    try{
		        Double.parseDouble(elem_from_ta[i][counter]);
		    }catch(NumberFormatException e){
		        parsable_elem = false;
		    }
		    //строка "весит" больше числа
			if(parsable_max && !parsable_elem){
				max = elem_from_ta[i][counter];
			}
			//если строки,сравниваем по ascii
			if(!parsable_max && max.compareTo(elem_from_ta[i][counter]) < 0){
				max = elem_from_ta[i][counter];
			}
			//если числа, сравниваем по значению
			if(parsable_max && parsable_elem && Double.parseDouble(max) < Double.parseDouble(elem_from_ta[i][counter])){
				max = elem_from_ta[i][counter];
			}
		}
		
		//находим количество макс элементов и добавляем во временное хранилище
		for(int i = 0; i < for_working.length; i++){
			if(max.compareTo(elem_from_ta[i][counter]) == 0){
				temp.add(for_working[i]);
			}
		}
		
		//проверяем ситуацию, если это был последний элемент в строке
		if(temp.size() != 1 && Collections.frequency(temp, temp.get(0)) != temp.size()){
		for(int i = 0; i < temp.size(); i++){
			if(temp.get(i).split("\t").length == counter + 1){
				temp.remove(temp.get(i));
			}
		}
		}
		
		//если в temp не один элемент или строки не одинаковы, то ищем "тяжелую" строку по след элементу
		if(temp.size() != 1 && Collections.frequency(temp, temp.get(0)) != temp.size()){
			String[] arr_str = temp.toArray(new String[temp.size()]);
			process(str_from_ta, arr_str, counter+1, result);
		}
		else{
				counter = 0;
				ArrayList<String> for_removing = new ArrayList<String>(Arrays.asList(for_working));
				for(int i = 0; i < temp.size(); i++)
				{
				result.add(temp.get(i));
				for_removing.remove(temp.get(i));
				}
				//если в исходном массиве осталась только одна строка
				if(for_removing.size() == 1){
					result.add(for_removing.get(0));
					for_removing.clear();
				}
				//если в result есть все строки str_from_ta - return
				if(result.containsAll(Arrays.asList(str_from_ta)) && Arrays.asList(str_from_ta).containsAll(result)){
					return;				
				}
				else{
					ArrayList<String> remainder = new ArrayList<String> (Arrays.asList(str_from_ta).size());
					remainder.addAll(Arrays.asList(str_from_ta));
					remainder.removeAll(result);
					String[] arr_str = remainder.toArray(new String[remainder.size()]);
					process(str_from_ta, arr_str, counter, result);
				}
			}	
		}
		catch(Exception e){
			Window.alert(e.getMessage());
		}
	}
	
}
