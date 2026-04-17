package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainMenu extends Menu{
    public MainMenu() {
        super(TypeMenu.MAIN);
    }

    @Override
    public void build() {
        Texture texture = new Texture(Gdx.files.internal("test/test.jpg"));
        Image testImg = new Image(texture);

        testImg.setOrigin(testImg.getWidth()/2,testImg.getHeight()/2);
        testImg.addAction(Actions.forever((Actions.rotateBy(360,2f))));

        Table table = new Table();
        table.setFillParent(true);
        table.add(testImg).center();

        stage.addActor(table);
    }
}
