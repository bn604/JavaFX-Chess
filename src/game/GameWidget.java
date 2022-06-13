package game;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

import java.util.Iterator;

public abstract class GameWidget<T extends Node> {

    public static final class Bloom
            extends EffectType {

        private final javafx.scene.effect.Bloom fxBloom;

        public Bloom(final double threshold) {

            fxBloom = new javafx.scene.effect.Bloom(threshold);

        }

        @Override
        public javafx.scene.effect.Bloom getFXEffect() {

            return fxBloom;
        }

        @Override
        public Effect chainEffect(final Effect effect) {

            fxBloom.setInput(effect);

            return fxBloom;
        }

    }
    
    public static final class Glow
            extends EffectType {
        
        private final javafx.scene.effect.Glow fxGlow;
        
        public Glow(final double radius) {

            fxGlow = new javafx.scene.effect.Glow(radius);
            
        }

        @Override
        public javafx.scene.effect.Glow getFXEffect() {

            return fxGlow;
        }
        
        @Override
        public Effect chainEffect(final Effect effect) {
            
            fxGlow.setInput(effect);
            
            return fxGlow;
        }
        
    }

    public static final class InnerShadow
            extends EffectType {

        private final javafx.scene.effect.InnerShadow fxInnerShadow;

        public InnerShadow(final double radius, final Color color) {
            
            fxInnerShadow = new javafx.scene.effect.InnerShadow(radius, color);
            
        }

        @Override
        public javafx.scene.effect.InnerShadow getFXEffect() {

            return fxInnerShadow;
        }
        
        @Override
        public Effect chainEffect(final Effect effect) {

            fxInnerShadow.setInput(effect);

            return fxInnerShadow;
        }
        
    }
    
    public static abstract class EffectType {

        private EffectType() {
            
            super();
            
        }

        public abstract Effect getFXEffect();
        
        public abstract Effect chainEffect(final Effect effect);
        
    }
    
    private final T node;
    
    private ObservableList<EffectType> effects;
    
    protected GameWidget(final T node) {
        
        this.node = node;
        
    }
    
    public final T getNode() {
        
        return node;
    }
    
    public final ObservableList<EffectType> getEffectTypes() {
        
        if (effects == null) {
            
            effects = FXCollections.observableArrayList();
            
            effects.addListener((ListChangeListener<EffectType>) change -> {
                
                final Iterator<? extends EffectType> iterator = change
                        .getList()
                        .iterator();
                
                Effect effect = null;
                
                while (iterator.hasNext()) {
                    
                    effect = iterator
                            .next()
                            .chainEffect(effect);
                    
                }
                
                node.setEffect(effect);
                
            });
            
        }
        
        return effects;
    }
    
}