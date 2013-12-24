package webui.tests.setup.configuration.managers;


import webui.tests.setup.actions.SetupAction;
import webui.tests.utils.CollectionUtils;

import java.util.List;

/**
 */
public abstract class SetupActionsManager {

    public void invokeActions(List<SetupAction> actions) {
        CollectionUtils.each(actions, new SetupAction.InvokeAction());
    }

    public static class TestNg extends SetupActionsManager{
        public List<SetupAction> beforeClass;
        public List<SetupAction> beforeTest;
        public List<SetupAction> afterTest;
        public List<SetupAction> afterClass;




        public void executeBeforeClass() {
            invokeActions(beforeClass);
        }

        public void executeAfterClass() {
            invokeActions(afterClass);
        }

        public void executeAfterTest() {
            invokeActions(afterTest);
        }

        public void executeBeforeTest() {
            invokeActions(beforeTest);
        }

        public List<SetupAction> getBeforeClass() {
            return beforeClass;
        }

        public void setBeforeClass(List<SetupAction> beforeClass) {
            this.beforeClass = beforeClass;
        }

        public List<SetupAction> getBeforeTest() {
            return beforeTest;
        }

        public void setBeforeTest(List<SetupAction> beforeTest) {
            this.beforeTest = beforeTest;
        }

        public List<SetupAction> getAfterTest() {
            return afterTest;
        }

        public void setAfterTest(List<SetupAction> afterTest) {
            this.afterTest = afterTest;
        }

        public List<SetupAction> getAfterClass() {
            return afterClass;
        }

        public void setAfterClass(List<SetupAction> afterClass) {
            this.afterClass = afterClass;
        }
    }

    public static class Main extends SetupActionsManager{
        public List<SetupAction> actions;

        public void invokeActions(){
            invokeActions(actions);
        }
    }


}
