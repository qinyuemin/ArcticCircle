package com.xiaoma.beiji.controls.view.chatting;


public interface OnKeyboardStateChangeListener {
    byte KEYBOARD_STATE_SHOW = -3;
    byte KEYBOARD_STATE_HIDE = -2;

    void onKeyBoardStateChange(int state);
}
