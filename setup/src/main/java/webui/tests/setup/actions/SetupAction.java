package webui.tests.setup.actions;

import webui.tests.utils.CollectionUtils;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/10/13
 * Time: 8:17 PM
 *
 * an interface allowing to run a simple setup action
 */
public interface SetupAction {

    public void invoke();

    public static class InvokeAction implements CollectionUtils.Action<SetupAction> {
        @Override
        public void apply(SetupAction item) {
            item.invoke();
        }
    }
}
