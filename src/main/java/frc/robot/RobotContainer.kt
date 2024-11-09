package frc.robot

import edu.wpi.first.wpilibj2.command.Command
//import edu.wpi.first.wpilibj2.command.Commands.runOnce
import edu.wpi.first.wpilibj2.command.Commands.*
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import edu.wpi.first.wpilibj2.command.button.Trigger

import frc.robot.commands.Autos
import frc.robot.commands.ExampleCommand
import frc.robot.subsystems.DriveTrain
import frc.robot.subsystems.ExampleSubsystem
import frc.robot.subsystems.IntakeSubsystem
import frc.robot.subsystems.ShooterSubsystem

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 *
 * In Kotlin, it is recommended that all your Subsystems are Kotlin objects. As such, there
 * can only ever be a single instance. This eliminates the need to create reference variables
 * to the various subsystems in this container to pass into to commands. The commands can just
 * directly reference the (single instance of the) object.
 */
object RobotContainer
{

    private val driverController = CommandXboxController(Constants.OperatorConstants.DRIVER_CONTROLLER_PORT)
    private val shooter = ShooterSubsystem
    private val intake = IntakeSubsystem
    private val driveTrain = DriveTrain

    init
    {
        configureBindings()
        // Reference the Autos object so that it is initialized, placing the chooser on the dashboard
        Autos
    }

    // Replace with CommandPS4Controller or CommandJoystick if needed

    /**
     * Use this method to define your `trigger->command` mappings. Triggers can be created via the
     * [Trigger] constructor that takes a [BooleanSupplier][java.util.function.BooleanSupplier]
     * with an arbitrary predicate, or via the named factories in [GenericHID][edu.wpi.first.wpilibj2.command.button.CommandGenericHID]
     * subclasses such for [Xbox][CommandXboxController]/[PS4][edu.wpi.first.wpilibj2.command.button.CommandPS4Controller]
     * controllers or [Flight joysticks][edu.wpi.first.wpilibj2.command.button.CommandJoystick].
     */
    private fun configureBindings()
    {
        // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
        Trigger { ExampleSubsystem.exampleCondition() }.onTrue(ExampleCommand())

        // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
        // cancelling on release.
        //driverController.b().whileTrue(ExampleSubsystem.exampleMethodCommand())
        driverController.rightTrigger().onTrue(Shoot())
        driverController.leftBumper().onTrue(Intake())
        driverController.rightBumper().onTrue(SpitOut())

        driveTrain.defaultCommand = driveTrain.getDriveCommand({
            driverController.getRawAxis(0)
        }, {
            driverController.getRawAxis(1)
        }, {
            driverController.getRawAxis(4)
        }, true)

        driverController.back().onTrue(Zero())
        driverController.start().onTrue(StopAll())
    }

    //Zero
    fun Zero(): Command{
        return runOnce({ driveTrain.zero()}, driveTrain)
    }

    //Stop Intake and Shooter
    fun StopAll(): Command{
        return runOnce({
            shooter.stop()
            intake.stop()
        }, shooter, intake)
    }

    //Intake
    fun Intake() : Command{
        if(!intake.hasNote()){
            return sequence(
                runOnce({ intake.Pull()}, intake),
                waitUntil({ intake.hasNote()})).finallyDo{-> intake.stop() }
        }
        else{return none()}
    }

    //Spit Out Note
    fun SpitOut() : Command{
        return sequence(
            runOnce({ intake.Push()}, intake),
            waitSeconds(1.0)
        ).finallyDo{-> intake.stop() }

    }

    //FIRE IN THE HOLE
    fun Shoot() : Command {
        return sequence(
            runOnce( {shooter.shoot()}, shooter),
            waitUntil({shooter.getShooterSpeed() >= 3000}),
            runOnce( { intake.Pull() }, intake),
            waitSeconds(0.5)).finallyDo{->
                shooter.stop()
                intake.stop()
            }
    }
}