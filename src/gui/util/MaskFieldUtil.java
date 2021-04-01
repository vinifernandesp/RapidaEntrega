package gui.util;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MaskFieldUtil {

	public static void maskCPF(TextField textField){

        textField.setOnKeyTyped((KeyEvent event) -> {
            if("0123456789".contains(event.getCharacter())==false){
                event.consume();
            }

            if(event.getCharacter().trim().length()==0){

                if(textField.getText().length()==4){
                    textField.setText(textField.getText().substring(0,3));
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==8){
                    textField.setText(textField.getText().substring(0,7));
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==12){
                    textField.setText(textField.getText().substring(0,11));
                    textField.positionCaret(textField.getText().length());
                }

            }else{

                if(textField.getText().length()==14) event.consume();

                if(textField.getText().length()==3){
                    textField.setText(textField.getText()+".");
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==7){
                    textField.setText(textField.getText()+".");
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==11){
                    textField.setText(textField.getText()+"-");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if(!textField.getText().matches("\\d.-*")){
                textField.setText(textField.getText().replaceAll("[^\\d.-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void maskCNPJ(TextField textField){

        textField.setOnKeyTyped((KeyEvent event) -> {
            if("0123456789".contains(event.getCharacter())==false){
                event.consume();
            }

            if(event.getCharacter().trim().length()==0){

                if(textField.getText().length()==3){
                    textField.setText(textField.getText().substring(0,2));
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==7){
                    textField.setText(textField.getText().substring(0,6));
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==11){
                    textField.setText(textField.getText().substring(0,10));
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==16){
                    textField.setText(textField.getText().substring(0,15));
                    textField.positionCaret(textField.getText().length());
                }

            }else{

                if(textField.getText().length()==18) event.consume();

                if(textField.getText().length()==2){
                    textField.setText(textField.getText()+".");
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==6){
                    textField.setText(textField.getText()+".");
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==10){
                    textField.setText(textField.getText()+"/");
                    textField.positionCaret(textField.getText().length());
                }
                if(textField.getText().length()==15){
                    textField.setText(textField.getText()+"-");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });
    }
}
