package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.card.Card;

public class CardListMenu extends Menu {
    public CardListMenu(MenuController controller) {
        super(TypeMenu.CARDLIST, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_biblio.png");

        addButton(
            "button/arrow_return.png",
            "button/arrow_return_gold.png",
            50,
            890,
            1.5f,
            controller::goMain,
            "sfx/Click_stereo.ogg.mp3"
        );

        addSearch(WIDTH / 2f - 700, 825, 540, this::refreshCards);

        addButton("button/search/btn_capitalist.png",
            "button/search/btn_capitalist_pressed.png",
            WIDTH - 900,
            800,
            1f,
            () -> toggleFamilyFilter("capitalistes"),ButtonMode.TOGGLE,"sfx/Click_stereo.ogg.mp3");

        addButton("button/search/btn_goofy.png",
            "button/search/btn_goofy_pressed.png",
            WIDTH - 700,
            800,
            1f,
            () -> toggleFamilyFilter("goofys"),ButtonMode.TOGGLE,"sfx/Click_stereo.ogg.mp3");

        addButton("button/search/btn_maudit.png",
            "button/search/btn_maudit_pressed.png",
            WIDTH - 500,
            800,
            1f,
            () -> toggleFamilyFilter("maudits"),ButtonMode.TOGGLE,"sfx/Click_stereo.ogg.mp3");

        addButton("button/search/btn_prodige.png",
            "button/search/btn_prodige_pressed.png",
            WIDTH - 300,
            800,
            1f,
            () -> toggleFamilyFilter("prodiges"),ButtonMode.TOGGLE,"sfx/Click_stereo.ogg.mp3");


        addAllCards(
            "cards",
            250,
            80,
            1450,
            700,
            150,
            220,
            65,
            70,
            7
        );

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }


}
