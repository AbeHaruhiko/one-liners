package jp.caliconography.one_liners.gesture;

public interface TranslationGestureListener {
    public void onTranslation(TranslationGestureDetector detector);

    public void onTranslationBegin(TranslationGestureDetector detector);

    public void onTranslationEnd(TranslationGestureDetector detector);
}