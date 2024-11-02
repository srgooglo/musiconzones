package com.moz.commands;

import com.moz.MusicOnZones;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.Command;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;


// Para enviar un evento desde el servidor al cliente para que el sonido se reproduzca en una ubicación específica, puedes usar paquetes (packets) en Fabric. Esto implica crear un paquete que el servidor envíe al cliente con la información necesaria, y luego hacer que el cliente reproduzca el sonido al recibir el paquete. Aquí te muestro cómo implementarlo:
//
// Paso a Paso
// Define el Identificador del Paquete:
//
// Crea un identificador para el evento de sonido. Este será el canal por el cual el servidor y el cliente se comunicarán.
//
// java
// Copiar código
// public static final Identifier PLAY_AMBIENT_SOUND_PACKET_ID = new Identifier("tu_modid", "play_ambient_sound");
// Crear el Paquete y el Método de Envío:
//
// Crea un método en el servidor que enviará el paquete al cliente especificando el sonido y la posición.
//
// java
// Copiar código
// import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
// import net.minecraft.network.PacketByteBuf;
// import net.minecraft.server.network.ServerPlayerEntity;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.math.Vec3d;
// import net.minecraft.network.PacketByteBuf;
// import net.minecraft.sound.SoundEvent;
// import net.minecraft.registry.Registries;
// import net.minecraft.registry.Registry;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.math.Vec3d;
// import net.minecraft.server.network.ServerPlayerEntity;
// import io.netty.buffer.Unpooled;
//
// public class SoundPacketHandler {
//     public static final Identifier PLAY_AMBIENT_SOUND_PACKET_ID = new Identifier("tu_modid", "play_ambient_sound");
//
//     public static void sendPlaySoundPacket(ServerPlayerEntity player, SoundEvent sound, Vec3d position, float volume, float pitch) {
//         PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
//
//         // Escribe los datos en el paquete
//         buf.writeIdentifier(Registries.SOUND_EVENT.getId(sound));
//         buf.writeDouble(position.x);
//         buf.writeDouble(position.y);
//         buf.writeDouble(position.z);
//         buf.writeFloat(volume);
//         buf.writeFloat(pitch);
//
//         // Envía el paquete al cliente
//         ServerPlayNetworking.send(player, PLAY_AMBIENT_SOUND_PACKET_ID, buf);
//     }
// }
// Este método toma como parámetros al jugador (ServerPlayerEntity), el evento de sonido (SoundEvent), la posición (Vec3d), el volumen y el tono.
//
// Los datos se escriben en PacketByteBuf, y luego se envía el paquete al cliente usando ServerPlayNetworking.send.
//
// Registrar el Receptor del Paquete en el Cliente:
//
// En el cliente, debes registrar un receptor para este paquete para que reproduzca el sonido cuando llegue.
//
// java
// Copiar código
// import net.fabricmc.api.ClientModInitializer;
// import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.sound.PositionedSoundInstance;
// import net.minecraft.network.PacketByteBuf;
// import net.minecraft.sound.SoundEvent;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.math.Vec3d;
//
// public class ClientInitializer implements ClientModInitializer {
//     @Override
//     public void onInitializeClient() {
//         // Registra el receptor del paquete en el cliente
//         ClientPlayNetworking.registerGlobalReceiver(SoundPacketHandler.PLAY_AMBIENT_SOUND_PACKET_ID, (client, handler, buf, responseSender) -> {
//             // Lee los datos del paquete
//             Identifier soundId = buf.readIdentifier();
//             double x = buf.readDouble();
//             double y = buf.readDouble();
//             double z = buf.readDouble();
//             float volume = buf.readFloat();
//             float pitch = buf.readFloat();
//
//             // Obtiene el evento de sonido a partir del identificador
//             SoundEvent sound = Registries.SOUND_EVENT.get(soundId);
//
//             // Ejecuta en el hilo del cliente para que se reproduzca el sonido
//             client.execute(() -> {
//                 if (sound != null && client.player != null) {
//                     Vec3d position = new Vec3d(x, y, z);
//                     client.getSoundManager().play(new PositionedSoundInstance(
//                             sound, volume, pitch, (float) position.x, (float) position.y, (float) position.z));
//                 }
//             });
//         });
//     }
// }
// Este código lee los datos enviados en el paquete (soundId, x, y, z, volume, y pitch) y luego usa PositionedSoundInstance para reproducir el sonido en el cliente en la ubicación especificada.
//
// Registrar el Paquete en el Servidor (Opcional):
//
// Si planeas usar este paquete desde varios lugares del código, puedes centralizar su registro en una clase de inicialización común.
// Llamar al Método desde el Servidor:
//
// Finalmente, llama al método SoundPacketHandler.sendPlaySoundPacket desde el servidor en el momento adecuado (por ejemplo, cuando ocurra un evento específico). Aquí tienes un ejemplo básico:
//
// java
// Copiar código
// Vec3d soundPosition = new Vec3d(100, 64, 100);
// SoundPacketHandler.sendPlaySoundPacket(player, SoundEvents.AMBIENT_CAVE, soundPosition, 1.0F, 1.0F);
// Con este enfoque, el servidor puede notificar al cliente sobre eventos de sonido en ubicaciones específicas, y el cliente reproducirá el sonido al recibir el paquete.

public class Commands {
    public static int spawnFox(MinecraftServer mc_server, ServerCommandSource source, String[] args) {
        int numOfSpawns = Integer.parseInt(args[0]);

        ServerWorld world = source.getWorld();
        Vec3d pos = source.getPosition();

        for (int i = 0; i < numOfSpawns; i++) {
            FoxEntity fox = EntityType.FOX.create(world);
            fox.setPos(pos.x, pos.y, pos.z);
            world.spawnEntity(fox);
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int createZone(MinecraftServer mc_server, ServerCommandSource source, String[] args) {
        return Command.SINGLE_SUCCESS;
    }

    public static int deleteZone(MinecraftServer mc_server, ServerCommandSource source, String[] args) {
        return Command.SINGLE_SUCCESS;
    }

    public static int modifyZone(MinecraftServer mc_server, ServerCommandSource source, String[] args) {
        return Command.SINGLE_SUCCESS;
    }
}
