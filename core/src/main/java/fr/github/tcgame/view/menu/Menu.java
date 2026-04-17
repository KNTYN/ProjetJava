package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Menu {
    protected Stage stage;
    public enum TypeMenu {MAIN,SELECTION}
    public TypeMenu typeMenu;

    public Menu(TypeMenu typeMenu){
        this.typeMenu=typeMenu;
        this.stage=new Stage(new ScreenViewport()); // Le viewport s'adapte à l'écran
        this.build();
    }

    public void draw(){
        this.stage.act(Gdx.graphics.getDeltaTime());
        this.stage.draw();
    }

    public void dispose(){
        this.stage.dispose();
    }

    public Stage getStage(){
        return this.stage; // getter
    }

    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    public abstract void build(); // Permet de créer un bouton
}
