package com.kullmar.runemar.bot;

import com.kullmar.runemar.api.InteractableObject;
import com.kullmar.runemar.api.LocalPoint;

public interface Bot {
    void interactWith(InteractableObject object);
    void moveTo(LocalPoint location);
}
