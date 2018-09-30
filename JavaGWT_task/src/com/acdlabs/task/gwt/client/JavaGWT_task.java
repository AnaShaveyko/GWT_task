package com.acdlabs.task.gwt.client;

import java.util.ArrayList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class JavaGWT_task implements EntryPoint {
	private KeyDownHandler handler = new KeyDownHandler(){
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == 9){
			event.preventDefault();
			event.stopPropagation();
			if(event.getSource() instanceof TextArea) {
		        TextArea ta = (TextArea) event.getSource();
		        int index = ta.getCursorPos();
		        String text = ta.getText();
		        ta.setText(text.substring(0, index) 
		                   + "\t" + text.substring(index));
		        ta.setCursorPos(index + 1);
		      }
			}
			if(event.getNativeKeyCode() == 32)
			{
				event.preventDefault();
				event.stopPropagation();
			}
		}
	};
	private Storage stockStore = null;
	private HorizontalPanel horizontalPanel = new HorizontalPanel();
	private TextArea inputTextarea = new TextArea();
	private Button processButton = new Button("Process");
	private Grid sortGrid = new Grid();
	@Override
	public void onModuleLoad() {
		
		//add elements
		inputTextarea.setCharacterWidth(40);
		inputTextarea.setVisibleLines(15);
		inputTextarea.addKeyDownHandler(handler);
		
		sortGrid.setBorderWidth(1);
		sortGrid.setCellPadding(3);

		horizontalPanel.add(inputTextarea);
		horizontalPanel.add(sortGrid);
		horizontalPanel.add(processButton);
		
		RootPanel.get("sorting").add(horizontalPanel);
		
		//запоминаем последнюю введенную инфу
		stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null){
		  for (int i = 0; i < stockStore.getLength(); i++){
		    String key = stockStore.key(i);
		    
		    String  str_storage = inputTextarea.getText();
			str_storage += stockStore.getItem(key);

			inputTextarea.setText(str_storage);
		  }
		} 		
		
		processButton.addClickHandler(new ClickHandler() {
	          @Override
	          public void onClick(ClickEvent event) {
	        	// clear Storage
					stockStore = Storage.getLocalStorageIfSupported();
					 if (stockStore !=null) {
					      stockStore.clear();
					 }
					 
				//add new data to storage	 
	             final String inputtext = inputTextarea.getText();
	             stockStore = Storage.getLocalStorageIfSupported();
	             if (stockStore != null) {
					  int numStocks = stockStore.getLength();
					  stockStore.setItem("Stock."+numStocks, inputtext);
					}
	             
	             //обработка наличия пустых строк
	             String new_text = new String();
	             new_text = inputTextarea.getText().replaceAll("\n\n", "\n \n");
	             //разделяем исходный текст на строки
	             String str_from_textarea [] = new_text.split("\n");
	             //результат сортировки	             
	             ArrayList<String> result = new ArrayList<String>(str_from_textarea.length);
	             //массив строк для сортировки
	             String for_working [] = str_from_textarea;
	             //сортировка         
	             Process sorting = new Process();
	             sorting.process(str_from_textarea, for_working, 0, result);
	             //выводим отсортированную инфу в grid	 
	     		fillGrid(result);
	          }
	       });	
	}	
	private void fillGrid(ArrayList<String> result){
	String result_arr[] = result.toArray(new String[result.size()]);
    String[][] elem_from_textarea = new String[result_arr.length][];
    int num_row = result_arr.length;
    int num_column = 0;
    int num_of_result = 0;
    for (int i=result_arr.length -1 ; i >= 0; i--){
        elem_from_textarea[i] = result_arr[num_of_result++].split("\t");
        if(elem_from_textarea[i].length > num_column)
				num_column = elem_from_textarea[i].length;
    }
	sortGrid.resize(num_row, num_column);
	//очищаем предыдущее
	for(int row = 0; row < sortGrid.getRowCount(); row++)
		for(int column = 0; column < sortGrid.getColumnCount(); column++)
			sortGrid.clearCell(row, column);
    int numRows = sortGrid.getRowCount();
    int numColumns = sortGrid.getColumnCount();
    for (int row = 0; row < numRows; row++) {
       for (int col = 0; col < numColumns; col++) {
          sortGrid.setText(row, col, elem_from_textarea[row][col]);
          }
       }
    }
	
}

