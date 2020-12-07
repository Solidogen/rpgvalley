import com.soywiz.korge.*
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.*

suspend fun main() = Korge(width = 400, height = 400) {
	val tiledMap = resourcesVfs["zeldagfx/overworld_tiled_map.tmx"].readTiledMap()
	val collisionMaskLayer = tiledMap.objectLayers.find { it.name == "CollisionMask" }
	collisionMaskLayer?.visible = false
	fixedSizeContainer(400,400, clip = true) {
		position(0,0)
		val camera = camera {
			tiledMapView(tiledMap)
		}
	}
	val circle = circle(radius = 10.0, fill = Colors.BLUE).apply {
		addUpdater {
			x = mouseX
			y = mouseY
		}
		onCollision(filter = {
			true
		}) {
			var touchesObjectBounds = false
			collisionMaskLayer?.objects.orEmpty().map { it.bounds }.forEach { objectBounds ->
				if (this.globalBounds.intersects(objectBounds)) {
					touchesObjectBounds = true
				}
			}
			fill = if (touchesObjectBounds) Colors.RED else Colors.BLUE
		}
	}
}