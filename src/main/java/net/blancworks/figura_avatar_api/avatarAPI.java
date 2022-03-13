package net.blancworks.figura_avatar_api;

import net.blancworks.figura.FiguraMod;
import net.blancworks.figura.avatar.AvatarDataManager;
import net.blancworks.figura.lua.CustomScript;
import net.blancworks.figura.lua.api.FiguraAPI;
import net.minecraft.util.Identifier;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.nio.file.Files;
import java.nio.file.Path;

public class avatarAPI implements FiguraAPI {
    @Override
    public Identifier getID() {
        return new Identifier("custom_avatar_api", "avatar");
    }

    //variable to add delay between uploading to backend
    private long uploadDelay = 0;

    @Override
    public LuaTable getForScript(CustomScript script) {
        return new LuaTable() {{

            //load avatar from backend
            set("loadFromBackend", new ZeroArgFunction() {
                @Override
                public LuaValue call() {
                    if (script.avatarData == AvatarDataManager.localPlayer) {
                        AvatarDataManager.clearLocalPlayer();
                    }
                    return NIL;
                }
            });

            //reload avatar
            set("reload", new ZeroArgFunction() {
                @Override
                public LuaValue call() {
                    if (script.avatarData == AvatarDataManager.localPlayer) {
                        AvatarDataManager.localPlayer.reloadAvatar();
                    }
                    return NIL;
                }
            });

            //upload avatar to backend
            set("uploadToBackend", new ZeroArgFunction() {
                @Override
                public LuaValue call() {
                    if (script.avatarData == AvatarDataManager.localPlayer) {
                        long currentTime = System.currentTimeMillis();
                        if ( currentTime > uploadDelay ) {
                            uploadDelay = System.currentTimeMillis() + 2500; //adds a bit of delay between uploading
                            FiguraMod.networkManager.postAvatar().thenRun(() -> System.out.println("UPLOADED AVATAR"));
                            return LuaValue.valueOf(true);
                        } else {
                            return LuaValue.valueOf(false);
                        }
                    }
                    return NIL;
                }
            });

            //check if avatar is local
            set("isLocal", new ZeroArgFunction() {
                @Override
                public LuaValue call() { {
                    return LuaValue.valueOf( script.avatarData.isLocalAvatar );
                }}
            });

            //set avatar
            set("set", new OneArgFunction() {
                @Override
                public LuaValue call(LuaValue arg1) {
                    if (script.avatarData == AvatarDataManager.localPlayer) {
                        AvatarDataManager.localPlayer.isLocalAvatar = true;

                        String figuraPath = FiguraMod.getModContentDirectory().toString();

                        String lastLetter = figuraPath.substring(figuraPath.length() - 1);

                        if (lastLetter.equals("/") || lastLetter.equals("\\") ) {
                            figuraPath = figuraPath.substring(0, figuraPath.length() - 1);
                        }

                        String luaPath = arg1.toString();

                        if (luaPath.charAt(0) == '/') {
                            luaPath = luaPath.substring(1);
                        }

                        String path = figuraPath + "/model_files/" + luaPath;

                        if ( Files.exists(Path.of(path)) ) {
                            AvatarDataManager.localPlayer.loadModelFile(path);
                        } else {
                            return LuaValue.valueOf("avatar not found");
                        }
                    }
                    return NIL;
                }
            });
        }};
    }
}
